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

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.Codec2;
import com.alibaba.dubbo.remoting.buffer.ChannelBuffer;
import com.alibaba.dubbo.remoting.buffer.ChannelBuffers;
import com.alibaba.dubbo.remoting.buffer.DynamicChannelBuffer;
import com.alibaba.dubbo.remoting.exchange.Request;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.IOException;

/**
 * NettyCodecAdapter.
 *
 * @author qian.lei
 */
final class NettyCodecAdapter {

    private final ChannelOutboundHandler encoder = new InternalEncoder();

    private final ChannelInboundHandler decoder = new InternalDecoder();

    private final Codec2 codec;

    private final URL url;

    private final int bufferSize;

    private final com.alibaba.dubbo.remoting.ChannelHandler handler;

    public NettyCodecAdapter(Codec2 codec, URL url, com.alibaba.dubbo.remoting.ChannelHandler handler) {
        this.codec = codec;
        this.url = url;
        this.handler = handler;
        int b = url.getPositiveParameter(Constants.BUFFER_KEY, Constants.DEFAULT_BUFFER_SIZE);
        this.bufferSize = b >= Constants.MIN_BUFFER_SIZE && b <= Constants.MAX_BUFFER_SIZE ? b : Constants.DEFAULT_BUFFER_SIZE;
    }

    public ChannelOutboundHandler getEncoder() {
        return encoder;
    }

    public ChannelInboundHandler getDecoder() {
        return decoder;
    }

    @ChannelHandler.Sharable
    private class InternalEncoder extends MessageToByteEncoder<Object> {
        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
//            System.out.println("[==========>ecode...." + msg + "]");
//            System.out.println("handler:" + handler.getClass().getCanonicalName());
            ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
            NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url, handler);
            try {
                codec.encode(channel, buffer, msg);
            } finally {
                NettyChannel.removeChannelIfDisconnected(ctx.channel());
            }
            out.writeBytes(ChannelBuffers.wrappedBuffer(buffer.toByteBuffer()).array());
        }
//        @Override
//        protected void encode(ChannelHandlerContext ctx, Request msg, List out) throws Exception {
//            System.out.println("[==========>ecode...." + msg + "]");
//            System.out.println("handler:" + handler.getClass().getCanonicalName());
//            ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
//            NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url, handler);
//            try {
//                codec.encode(channel, buffer, msg);
//            } finally {
//                NettyChannel.removeChannelIfDisconnected(ctx.channel());
//            }
//            out.add(ChannelBuffers.wrappedBuffer(buffer.toByteBuffer()));
//        }

    }

    private class InternalDecoder extends SimpleChannelInboundHandler<ByteBuf> {

        private com.alibaba.dubbo.remoting.buffer.ChannelBuffer buffer =
                com.alibaba.dubbo.remoting.buffer.ChannelBuffers.EMPTY_BUFFER;

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf object) throws Exception {
//            System.out.println("[===========>Read From Remote is " + object + "]");
            int readable = object.readableBytes();
            if (readable <= 0) return;
            ByteBuf input = object;
            ChannelBuffer message;
            if (buffer.readable()) {
                if (buffer instanceof DynamicChannelBuffer) {
                    buffer.writeBytes(input.nioBuffer());
                    message = buffer;
                } else {
                    int size = buffer.readableBytes() + input.readableBytes();
                    message = com.alibaba.dubbo.remoting.buffer.ChannelBuffers.dynamicBuffer(
                            size > bufferSize ? size : bufferSize);
                    message.writeBytes(buffer, buffer.readableBytes());
                    message.writeBytes(input.nioBuffer());
                }
            } else {
                message = com.alibaba.dubbo.remoting.buffer.ChannelBuffers.wrappedBuffer(
                        input.nioBuffer());
            }
            NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url, handler);
            Object msg;
            int saveReaderIndex;

            try {
                // decode object.
                do {
                    saveReaderIndex = message.readerIndex();
                    try {
                        msg = codec.decode(channel, message);
                    } catch (IOException e) {
                        buffer = com.alibaba.dubbo.remoting.buffer.ChannelBuffers.EMPTY_BUFFER;
                        throw e;
                    }
                    if (msg == Codec2.DecodeResult.NEED_MORE_INPUT) {
                        message.readerIndex(saveReaderIndex);
                        break;
                    } else {
                        if (saveReaderIndex == message.readerIndex()) {
                            buffer = com.alibaba.dubbo.remoting.buffer.ChannelBuffers.EMPTY_BUFFER;
                            throw new IOException("Decode without read data.");
                        }
                        if (msg != null) {
                            ctx.fireChannelRead(msg);
//                            Channels.fireMessageReceived(ctx, msg, event.getRemoteAddress());
                        }
                    }
                } while (message.readable());
            } finally {
                if (message.readable()) {
                    message.discardReadBytes();
                    buffer = message;
                } else {
                    buffer = com.alibaba.dubbo.remoting.buffer.ChannelBuffers.EMPTY_BUFFER;
                }
                NettyChannel.removeChannelIfDisconnected(ctx.channel());
            }
        }


//        @Override
//        public void messageReceived(ChannelHandlerContext ctx, MessageEvent event) throws Exception {
//            Object o = event.getMessage();
//            if (!(o instanceof ChannelBuffer)) {
//                ctx.sendUpstream(event);
//                return;
//            }
//
//            ChannelBuffer input = (ChannelBuffer) o;
//            int readable = input.readableBytes();
//            if (readable <= 0) {
//                return;
//            }
//
//            com.alibaba.dubbo.remoting.buffer.ChannelBuffer message;
//            if (buffer.readable()) {
//                if (buffer instanceof DynamicChannelBuffer) {
//                    buffer.writeBytes(input.toByteBuffer());
//                    message = buffer;
//                } else {
//                    int size = buffer.readableBytes() + input.readableBytes();
//                    message = com.alibaba.dubbo.remoting.buffer.ChannelBuffers.dynamicBuffer(
//                            size > bufferSize ? size : bufferSize);
//                    message.writeBytes(buffer, buffer.readableBytes());
//                    message.writeBytes(input.toByteBuffer());
//                }
//            } else {
//                message = com.alibaba.dubbo.remoting.buffer.ChannelBuffers.wrappedBuffer(
//                        input.toByteBuffer());
//            }
//
//            NettyChannel channel = NettyChannel.getOrAddChannel(ctx.getChannel(), url, handler);
//            Object msg;
//            int saveReaderIndex;
//
//            try {
//                // decode object.
//                do {
//                    saveReaderIndex = message.readerIndex();
//                    try {
//                        msg = codec.decode(channel, message);
//                    } catch (IOException e) {
//                        buffer = com.alibaba.dubbo.remoting.buffer.ChannelBuffers.EMPTY_BUFFER;
//                        throw e;
//                    }
//                    if (msg == Codec2.DecodeResult.NEED_MORE_INPUT) {
//                        message.readerIndex(saveReaderIndex);
//                        break;
//                    } else {
//                        if (saveReaderIndex == message.readerIndex()) {
//                            buffer = com.alibaba.dubbo.remoting.buffer.ChannelBuffers.EMPTY_BUFFER;
//                            throw new IOException("Decode without read data.");
//                        }
//                        if (msg != null) {
//                            Channels.fireMessageReceived(ctx, msg, event.getRemoteAddress());
//                        }
//                    }
//                } while (message.readable());
//            } finally {
//                if (message.readable()) {
//                    message.discardReadBytes();
//                    buffer = message;
//                } else {
//                    buffer = com.alibaba.dubbo.remoting.buffer.ChannelBuffers.EMPTY_BUFFER;
//                }
//                NettyChannel.removeChannelIfDisconnected(ctx.getChannel());
//            }
//        }
//
//        @Override
//        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
//            ctx.sendUpstream(e);
//        }
    }
}