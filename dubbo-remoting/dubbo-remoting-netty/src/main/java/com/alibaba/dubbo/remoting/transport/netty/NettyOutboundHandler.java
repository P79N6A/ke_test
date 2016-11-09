package com.alibaba.dubbo.remoting.transport.netty;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * Created by chengtianliang on 2016/11/4.
 */
@io.netty.channel.ChannelHandler.Sharable
public class NettyOutboundHandler extends ChannelOutboundHandlerAdapter {
    private final URL url;

    private final ChannelHandler handler;

    public NettyOutboundHandler(URL url, ChannelHandler handler) {

        this.url = url;
        this.handler = handler;
    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        super.read(ctx);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url, handler);
        try {
            handler.sent(channel,msg);
        } finally {
            NettyChannel.removeChannelIfDisconnected(ctx.channel());
        }
        super.write(ctx, msg, promise);
    }
}
