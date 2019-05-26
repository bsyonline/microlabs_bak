/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.service;

import com.rolex.microlabs.cache.annotation.Cache;
import com.rolex.microlabs.dao.RedisDao;
import com.rolex.microlabs.dao.UserRepository;
import com.rolex.microlabs.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author rolex
 * @since 2019
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RedisDao redisDao;

    public User findById(int id) {
        User user = (User) redisDao.get(String.valueOf(id), User.class);
        if (user == null) {
            log.info("query redis not hits and get from db: key={}", id);
            user = userRepository.findById(id).orElseGet(null);
            if (user != null) {
                redisDao.put(String.valueOf(user.getId()), user);
                log.info("put into redis: key={}", id);
            }
        }
        return user;
    }

    @Cache(key = "#id", returnType = User.class)
    public User findByIdWithCache(int id) {
        return userRepository.findById(id).orElseGet(null);
    }
}
