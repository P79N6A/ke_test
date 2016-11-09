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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.ChannelHandler;

/**
 * NettyInboundHandler
 *
 * @author william.liangf
 */
@io.netty.channel.ChannelHandler.Sharable
public class NettyInboundHandler extends ChannelInboundHandlerAdapter {

    private final Map<String, Channel> channels = new ConcurrentHashMap<String, Channel>(); // <ip:port, channel>

    private final URL url;

    private final ChannelHandler handler;

    public NettyInboundHandler(URL url, ChannelHandler handler) {
        if (url == null) {
            throw new IllegalArgumentException("url == null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("handler == null");
        }
        this.url = url;
        this.handler = handler;
    }

    public Map<String, Channel> getChannels() {
        return channels;
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url, handler);
        try {
            if (channel != null) {
                channels.put(NetUtils.toAddressString((InetSocketAddress) ctx.channel().remoteAddress()), channel);
            }
            handler.connected(channel);
        } finally {
            NettyChannel.removeChannelIfDisconnected(ctx.channel());

        }
        super.channelActive(ctx);

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url, handler);
        try {
            channels.remove(NetUtils.toAddressString((InetSocketAddress) ctx.channel().remoteAddress()));
            handler.disconnected(channel);
        } finally {
            NettyChannel.removeChannelIfDisconnected(ctx.channel());

        }
        super.channelInactive(ctx);

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url, handler);
        try {
            handler.received(channel, msg);
        } finally {
            NettyChannel.removeChannelIfDisconnected(ctx.channel());
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url, handler);
        try {
            handler.caught(channel, null);
        } finally {
            NettyChannel.removeChannelIfDisconnected(ctx.channel());
        }
        super.exceptionCaught(ctx, cause);
    }

    //    @Override
//    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
//        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.getChannel(), url, handler);
//        try {
//            if (channel != null) {
//                channels.put(NetUtils.toAddressString((InetSocketAddress) ctx.getChannel().getRemoteAddress()), channel);
//            }
//            handler.connected(channel);
//        } finally {
//            NettyChannel.removeChannelIfDisconnected(ctx.getChannel());
//        }
//    }
//
//    @Override
//    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
//        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.getChannel(), url, handler);
//        try {
//            channels.remove(NetUtils.toAddressString((InetSocketAddress) ctx.getChannel().getRemoteAddress()));
//            handler.disconnected(channel);
//        } finally {
//            NettyChannel.removeChannelIfDisconnected(ctx.getChannel());
//        }
//    }
//
//    @Override
//    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
//        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.getChannel(), url, handler);
//        try {
//            handler.received(channel, e.getMessage());
//        } finally {
//            NettyChannel.removeChannelIfDisconnected(ctx.getChannel());
//        }
//    }
//
//    @Override
//    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
//        super.writeRequested(ctx, e);
//        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.getChannel(), url, handler);
//        try {
//            handler.sent(channel, e.getMessage());
//        } finally {
//            NettyChannel.removeChannelIfDisconnected(ctx.getChannel());
//        }
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
//        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.getChannel(), url, handler);
//        try {
//            handler.caught(channel, e.getCause());
//        } finally {
//            NettyChannel.removeChannelIfDisconnected(ctx.getChannel());
//        }
//    }

}