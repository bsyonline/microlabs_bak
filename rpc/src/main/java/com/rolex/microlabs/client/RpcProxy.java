/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author rolex
 * @since 2019
 */
public class RpcProxy implements InvocationHandler {

    private Object target;
    private static RpcClient client;

    public RpcProxy setTarget(Object target) {
        this.target = target;
        return this;
    }

    public Object newProxy() {//将目标对象传入进行代理
        if (client == null) {
            client = new RpcClient();
            client.connect();
            System.out.println("rpc client created");
        }
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(target, args);
    }
}
