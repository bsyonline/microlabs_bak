/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author rolex
 * @since 2019
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter { // (1) ChannelInboundHandler 实现,
                                                                         // 提供了多种 event handler 方法实现

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2) 重写 channelRead() 方法,
                                                                     // 从 client 接受消息,然后丢弃,消息类型为 ByteBuf
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.getBytes(0, bytes);
        System.out.println("receive msg : " + new String(bytes));
        //Discard the received data silently.
        ((ByteBuf) msg).release(); // (3) DISCARD protocol 的实现
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4) 异常事件, 捕获异常时关闭连接
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}