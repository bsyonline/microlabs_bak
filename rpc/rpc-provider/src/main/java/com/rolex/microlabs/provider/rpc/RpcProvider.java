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
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author rolex
 * @since 2019
 */
@Slf4j
public class RpcProvider {
    private Map<String, Object> handlerMap = new HashMap<>();

    public void start() throws UnknownHostException {

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
            String ip = getHost();
            int port = 8001;
            // 启动 RPC 服务器
            ChannelFuture future = bootstrap.bind(ip, port).sync();
            // 注册 RPC 服务地址
            Jedis jedis = jedisPool().getResource();
            jedis.set(serviceName, ip + ":" + port);
            log.info("register service to registry center, service={}, host={}, port={}", serviceName, ip, port);
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

    private String getHost() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress().toString();
    }

    public JedisPool jedisPool() {
        String host = "localhost";
        int port = 6379;
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(50);
        config.setMaxIdle(10);
        config.setMaxWaitMillis(1000 * 100);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        return new JedisPool(config, host, port);
    }

    public static void main(String[] args) throws UnknownHostException {
        new RpcProvider().start();
    }
}
