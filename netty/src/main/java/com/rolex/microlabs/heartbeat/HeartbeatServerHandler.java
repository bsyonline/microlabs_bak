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
public class HeartbeatServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object data) throws Exception {
        ByteBuf buf = (ByteBuf) data;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.getBytes(0, bytes);
        String msg = new String(bytes);
        if ("PING".equals(msg)) {
            log.info("receive msg {} and response PONG, client is alive", new String(bytes));
            ctx.writeAndFlush(Unpooled.copiedBuffer("PONG", UTF_8));
        } else {
            log.info("receive msg {} and response same msg", msg);
            ctx.writeAndFlush(Unpooled.copiedBuffer(msg, UTF_8));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    log.info("read idle and close connect");
                    ctx.close().sync();
                    break;
                case WRITER_IDLE:
                    log.info("write idle");
                    break;
                case ALL_IDLE:
                    log.info("all idle");
                    break;
                default:
                    break;
            }
        }
    }
}
