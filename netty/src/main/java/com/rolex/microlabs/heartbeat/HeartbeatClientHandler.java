/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.heartbeat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import static io.netty.util.CharsetUtil.UTF_8;

/**
 * @author rolex
 * @since 2019
 */
@Slf4j
public class HeartbeatClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.getBytes(0, bytes);
        log.info("receive msg {}", new String(bytes));
    }

//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        while (true) {
//            log.info("send msg {} to server", "hello");
//            ctx.writeAndFlush(Unpooled.copiedBuffer("hello", UTF_8));
//            Thread.sleep(15000);
//        }
//    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    log.info("read idle");
                    break;
                case WRITER_IDLE:
                    log.info("write idle");
                    break;
                case ALL_IDLE:
                    log.info("all idle and send PING");
                    ctx.writeAndFlush(Unpooled.copiedBuffer("PING", //2
                            UTF_8));
                    break;
            }
        }
    }
}
