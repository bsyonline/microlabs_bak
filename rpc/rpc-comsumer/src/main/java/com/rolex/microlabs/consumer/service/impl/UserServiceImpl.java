/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.consumer.service.impl;

import com.rolex.microlabs.consumer.rpc.RpcProxy;
import com.rolex.microlabs.consumer.service.UserService;
import com.rolex.microlabs.provider.model.User;

/**
 * @author rolex
 * @since 2019
 */
public class UserServiceImpl implements UserService {

    @Override
    public User findById(int id) {
        RpcProxy rpcProxy = new RpcProxy("");
        UserService userService = rpcProxy.create(UserService.class);
        return userService.findById(id);
    }
}
