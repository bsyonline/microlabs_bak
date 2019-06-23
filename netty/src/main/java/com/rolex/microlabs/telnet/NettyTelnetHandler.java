/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.telnet;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;
import java.util.Date;

/**
 * @author rolex
 * @since 2019
 */
public class NettyTelnetHandler extends SimpleChannelInboundHandler<String> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Send greeting for a new connection.
        ctx.write("Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
        ctx.write("It is " + new Date() + " now.\r\n");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        String response;
        boolean close = false;
        if (s.isEmpty()) {
            response = "please type the words...\r\n";
        } else if ("bye".equals(s.toLowerCase())) {
            response = "bye!\r\n";
            close = true;
        } else {
            response = "you said : " + s + "\r\n";
        }

        ChannelFuture future = channelHandlerContext.write(response);
        channelHandlerContext.flush();
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
