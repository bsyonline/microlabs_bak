/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.discard;

import io.netty.bootstrap.ServerBootstrap;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author rolex
 * @since 2019
 */
public class DiscardServer {

    private int port;

    public DiscardServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1); // (1) NioEventLoopGroup 是处理 IO 操作的多线程事件循环
                                                                       // boss EventLoopGroup 用来接收连接, boss 接收连接并注册
                                                                       // 到 work 后由 worker 处理
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2) 设置服务器
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3) 使用 NioServerSocketChannel 实例化连接
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DiscardServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5) 配置 server channel 参数
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6) childOption 用来配置 ServerSocket 建立连接
                                                                          // 后,由 ServerSocket 创建的socket的参数

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7) 绑定端口

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8009;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        new DiscardServer(port).run();
    }
}
