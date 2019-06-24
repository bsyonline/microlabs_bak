/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.consumer.rpc;

import com.rolex.microlabs.model.RpcRequest;
import com.rolex.microlabs.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @author rolex
 * @since 2019
 */
@Slf4j
public class RpcProxy {

    private String serviceAddress;

    public RpcProxy(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(final Class<?> interfaceClass) {
        return create(interfaceClass, "");
    }

    @SuppressWarnings("unchecked")
    public <T> T create(final Class<?> interfaceClass, final String serviceVersion) {
        // 创建动态代理对象
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // 创建 RPC 请求对象并设置请求属性
                        RpcRequest request = new RpcRequest();
                        request.setRequestId(UUID.randomUUID().toString());
                        request.setInterfaceName(method.getDeclaringClass().getName());
                        request.setServiceVersion(serviceVersion);
                        request.setMethodName(method.getName());
                        request.setParameterTypes(method.getParameterTypes());
                        request.setParameters(args);
                        // 获取 RPC 服务地址
                        Jedis jedis = jedisPool().getResource();
                        String serviceName = "com.rolex.microlabs.consumer.service.UserService";
                        String addr = jedis.get(serviceName);
                        // 从 RPC 服务地址中解析主机名与端口号
                        String ip = addr.split(":")[0];
                        Integer port = new Integer(addr.split(":")[1]);
                        // 创建 RPC 客户端对象并发送 RPC 请求
                        RpcClient client = new RpcClient(ip, port);
                        long time = System.currentTimeMillis();
                        RpcResponse response = client.send(request);
                        log.info("time: {}ms", (System.currentTimeMillis() - time));
                        if (response == null) {
                            throw new RuntimeException("response is null");
                        }
                        // 返回 RPC 响应结果
                        return response.getResult();

                    }
                }
        );
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

}