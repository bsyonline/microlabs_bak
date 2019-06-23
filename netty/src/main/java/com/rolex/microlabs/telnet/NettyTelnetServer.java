/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.telnet;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author rolex
 * @since 2019
 */
public class NettyTelnetServer {

    private static final int PORT = 8899;
    private ServerBootstrap serverBootstrap;
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    public void open() throws InterruptedException {
        serverBootstrap = new ServerBootstrap();
        //指定socket的属性
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        serverBootstrap.group(eventLoopGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new NettyTelnetInitializer());

        //绑定对应的端口号并启动监听端口的连接
        Channel channel = serverBootstrap.bind(PORT).sync().channel();

        //等待关闭
        channel.closeFuture().sync();
    }

}
