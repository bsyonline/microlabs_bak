/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.provider.rpc;

import com.rolex.microlabs.codec.RpcDecoder;
import com.rolex.microlabs.codec.RpcEncoder;
import com.rolex.microlabs.model.RpcRequest;
import com.rolex.microlabs.model.RpcResponse;
import com.rolex.microlabs.provider.service.impl.UserServiceImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rolex
 * @since 2019
 */
@Slf4j
public class RpcProvider {
    private Map<String, Object> handlerMap = new HashMap<>();

    public void start() {
        Object serviceBean = new UserServiceImpl();
        String serviceName = "com.rolex.microlabs.consumer.service.UserService";
        handlerMap.put(serviceName, serviceBean);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 创建并初始化 Netty 服务端 Bootstrap 对象
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast(new RpcDecoder(RpcRequest.class)); // 解码 RPC 请求
                    pipeline.addLast(new RpcEncoder(RpcResponse.class)); // 编码 RPC 响应
                    pipeline.addLast(new RpcProviderHandler(handlerMap)); // 处理 RPC 请求
                }
            });
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            // 获取 RPC 服务器的 IP 地址与端口号
//            String[] addressArray = StringUtil.split(serviceAddress, ":");
            String ip = "localhost";
            int port = 8081;
            // 启动 RPC 服务器
            ChannelFuture future = bootstrap.bind(ip, port).sync();
            // 注册 RPC 服务地址
//            if (serviceRegistry != null) {
//                for (String interfaceName : handlerMap.keySet()) {
//                    serviceRegistry.register(interfaceName, serviceAddress);
//                    LOGGER.debug("register service: {} => {}", interfaceName, serviceAddress);
//                }
//            }
            log.info("server started on port {}", port);
            // 关闭 RPC 服务器
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new RpcProvider().start();
    }
}
