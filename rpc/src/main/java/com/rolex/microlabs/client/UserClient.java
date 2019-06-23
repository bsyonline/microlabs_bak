/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.client;

import com.rolex.microlabs.UserService;

/**
 * @author rolex
 * @since 2019
 */
public class UserClient {

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        UserService proxy = (UserService) new RpcProxy().setTarget(userService).newProxy();
        proxy.findById("123");
    }
}
