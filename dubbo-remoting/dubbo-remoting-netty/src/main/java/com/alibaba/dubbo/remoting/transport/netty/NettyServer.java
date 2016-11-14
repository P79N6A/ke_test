/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.remoting.transport.netty;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;


import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ExecutorUtil;
import com.alibaba.dubbo.common.utils.NamedThreadFactory;
import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.Server;
import com.alibaba.dubbo.remoting.transport.AbstractServer;
import com.alibaba.dubbo.remoting.transport.dispatcher.ChannelHandlers;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * NettyServer
 *
 * @author qian.lei
 * @author chao.liuc
 */
public class NettyServer extends AbstractServer implements Server {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private Map<String, Channel> channels; // <ip:port, channel>

    private ServerBootstrap bootstrap;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private io.netty.channel.Channel channel;

    private static final int DEFAULT_BOSSGROUP_THREADS = Math.max(1,
            Runtime.getRuntime().availableProcessors() + 1);

    private static final int DEFAULT_WORKERGROUP_THREADS = Math.max(1,
            Runtime.getRuntime().availableProcessors() * 2);

    private int bossThreadNum = DEFAULT_BOSSGROUP_THREADS;

    private int workerThreadNum = DEFAULT_WORKERGROUP_THREADS;

    private int soBackLogNum = 1024;

    public NettyServer(URL url, ChannelHandler handler) throws RemotingException {
        super(url, ChannelHandlers.wrap(handler, ExecutorUtil.setThreadName(url, SERVER_THREAD_POOL_NAME)));
        if (url.hasParameter(Constants.IO_BOSS_THREADS_KEY)) {
            bossThreadNum = url.getPositiveParameter(Constants.IO_BOSS_THREADS_KEY, DEFAULT_BOSSGROUP_THREADS);
        }
        if (url.hasParameter(Constants.IO_WORKER_THREAD_KEY)) {
            workerThreadNum = url.getPositiveParameter(Constants.IO_WORKER_THREAD_KEY, DEFAULT_WORKERGROUP_THREADS);
        }
    }

    @Override
    protected void doOpen() throws Throwable {
        NettyHelper.setNettyLoggerFactory();
        bossGroup = new NioEventLoopGroup(bossThreadNum, new NamedThreadFactory("NettyServerBoss", true));
        workerGroup = new NioEventLoopGroup(workerThreadNum, new NamedThreadFactory("NettyServerWorker", true));
        bootstrap = new ServerBootstrap();
        final NettyInboundHandler nettyInboundHandler = new NettyInboundHandler(getUrl(), this);
        channels = nettyInboundHandler.getChannels();
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).
                childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(final SocketChannel ch) throws Exception {
                        NettyCodecAdapter adapter = new NettyCodecAdapter(getCodec(), getUrl(), NettyServer.this);
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("decoder", adapter.getDecoder());
                        pipeline.addLast("encoder", adapter.getEncoder());
                        pipeline.addLast("handlerIn", nettyInboundHandler);

                    }
                }).
                option(ChannelOption.SO_BACKLOG, soBackLogNum).
                childOption(ChannelOption.SO_REUSEADDR, true).
                childOption(ChannelOption.SO_KEEPALIVE, true);
//        ExecutorService boss = Executors.newCachedThreadPool(new NamedThreadFactory("NettyServerBoss", true));
//        ExecutorService worker = Executors.newCachedThreadPool(new NamedThreadFactory("NettyServerWorker", true));
//        ChannelFactory channelFactory = new NioServerSocketChannelFactory(boss, worker, getUrl().getPositiveParameter(Constants.IO_THREADS_KEY, Constants.DEFAULT_IO_THREADS));
//        bootstrap = new ServerBootstrap(channelFactory);
//
//        final NettyInboundHandler nettyInboundHandler = new NettyInboundHandler(getUrl(), this);
//        channels = nettyInboundHandler.getChannels();
//        // https://issues.jboss.org/browse/NETTY-365
//        // https://issues.jboss.org/browse/NETTY-379
//        // final Timer timer = new HashedWheelTimer(new NamedThreadFactory("NettyIdleTimer", true));
//        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
//            public ChannelPipeline getPipeline() {
//                NettyCodecAdapter adapter = new NettyCodecAdapter(getCodec() ,getUrl(), NettyServer.this);
//                ChannelPipeline pipeline = Channels.pipeline();
//                /*int idleTimeout = getIdleTimeout();
//                if (idleTimeout > 10000) {
//                    pipeline.addLast("timer", new IdleStateHandler(timer, idleTimeout / 1000, 0, 0));
//                }*/
//                pipeline.addLast("decoder", adapter.getDecoder());
//                pipeline.addLast("encoder", adapter.getEncoder());
//                pipeline.addLast("handler", nettyInboundHandler);
//                return pipeline;
//            }
//        });
//        // bind
        channel = bootstrap.bind(getBindAddress()).channel();
    }

    @Override
    protected void doClose() throws Throwable {
        try {
            if (channel != null) {
                // unbind.
                channel.close();
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
        try {
            Collection<com.alibaba.dubbo.remoting.Channel> channels = getChannels();
            if (channels != null && channels.size() > 0) {
                for (com.alibaba.dubbo.remoting.Channel channel : channels) {
                    try {
                        channel.close();
                    } catch (Throwable e) {
                        logger.warn(e.getMessage(), e);
                    }
                }
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
        try {
            if (workerGroup != null) {
                workerGroup.shutdownGracefully();
                workerGroup = null;
            }
            if (bossGroup != null) {
                bossGroup.shutdownGracefully();
                bossGroup = null;
            }
//            if (bootstrap != null) {
//                // release external resource.
//                bootstrap.
//            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
        try {
            if (channels != null) {
                channels.clear();
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
    }

    public Collection<Channel> getChannels() {
        Collection<Channel> chs = new HashSet<Channel>();
        for (Channel channel : this.channels.values()) {
            if (channel.isConnected()) {
                chs.add(channel);
            } else {
                channels.remove(NetUtils.toAddressString(channel.getRemoteAddress()));
            }
        }
        return chs;
    }

    public Channel getChannel(InetSocketAddress remoteAddress) {
        return channels.get(NetUtils.toAddressString(remoteAddress));
    }

    public boolean isBound() {
        return channel.isActive();
    }

}