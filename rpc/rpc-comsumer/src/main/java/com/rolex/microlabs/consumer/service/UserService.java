/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.consumer.service;

import com.rolex.microlabs.provider.model.User;

/**
 * @author rolex
 * @since 2019
 */
public interface UserService {
    User findById(int id);
}
