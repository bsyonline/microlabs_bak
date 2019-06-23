/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.provider.service.impl;

import com.rolex.microlabs.provider.model.User;
import com.rolex.microlabs.provider.service.UserService;

/**
 * @author rolex
 * @since 2019
 */
public class UserServiceImpl implements UserService {
    @Override
    public User findById(int id) {
        return new User(1, "tom", 20);
    }
}
