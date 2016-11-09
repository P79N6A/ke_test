package com.alibaba.dubbo.remoting.transport.netty;

import java.nio.ByteBuffer;


import com.alibaba.dubbo.remoting.buffer.ChannelBuffer;
import com.alibaba.dubbo.remoting.buffer.ChannelBufferFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Wrap netty dynamic channel buffer.
 *
 * @author <a href="mailto:gang.lvg@taobao.com">kimi</a>
 */
public class NettyBackedChannelBufferFactory implements ChannelBufferFactory {

    private static final NettyBackedChannelBufferFactory INSTANCE = new NettyBackedChannelBufferFactory();

    public static ChannelBufferFactory getInstance() {
        return INSTANCE;
    }


    public ChannelBuffer getBuffer(int capacity) {
        return new NettyBackedChannelBuffer(Unpooled.buffer(capacity));
    }


    public ChannelBuffer getBuffer(byte[] array, int offset, int length) {
        ByteBuf byteBuf = Unpooled.buffer(length);
        byteBuf.writeBytes(array, offset, length);
        return new NettyBackedChannelBuffer(byteBuf);
    }


    public ChannelBuffer getBuffer(ByteBuffer nioBuffer) {
        return new NettyBackedChannelBuffer(Unpooled.wrappedBuffer(nioBuffer));
    }
}
