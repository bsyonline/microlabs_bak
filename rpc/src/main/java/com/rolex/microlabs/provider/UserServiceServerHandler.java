/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.provider;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author rolex
 * @since 2019
 */
public class UserServiceServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2) 重写 channelRead() 方法,
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.getBytes(0, bytes);
        System.out.println("receive msg : " + new String(bytes));
        String result = new UserServiceServerImpl().findById(msg.toString());
        ctx.writeAndFlush(result);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4) 异常事件, 捕获异常时关闭连接
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
