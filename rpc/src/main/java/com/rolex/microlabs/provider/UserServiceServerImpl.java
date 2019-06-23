/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.provider;

import com.rolex.microlabs.UserService;

/**
 * @author rolex
 * @since 2019
 */
public class UserServiceServerImpl implements UserService {
    @Override
    public String findById(String id) {
        System.out.println("find by id on server: id=" + id);
        return "name: tom";
    }
}
