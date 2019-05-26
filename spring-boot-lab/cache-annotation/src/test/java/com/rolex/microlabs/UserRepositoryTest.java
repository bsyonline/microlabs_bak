/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import com.rolex.microlabs.model.User;
import com.rolex.microlabs.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author rolex
 * @since 2019
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UserRepositoryTest {

    @Autowired
    UserService userService;

    @Test
    public void findById(){
        User user = userService.findById(1);
        Assert.assertEquals("Tom", user.getName());
    }
    @Test
    public void findByIdWithCache(){
        User user = userService.findByIdWithCache(1);
        Assert.assertEquals("Tom", user.getName());
    }

}
