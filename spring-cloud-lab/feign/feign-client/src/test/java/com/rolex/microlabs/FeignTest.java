/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import com.rolex.microlabs.model.Order;
import com.rolex.microlabs.service.OrderService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rolex
 * @since 2019
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FeignTest {

    @Autowired
    OrderService orderService;

    @Test
    public void findById() {
        Order result = orderService.findById("1");
        Assert.assertEquals("1", result.getId());
    }

    @Test
    public void add() {
        Order result = orderService.add(new Order("3", LocalDateTime.now().toString(), "3"));
        Assert.assertEquals("3", result.getId());
    }

    @Test
    public void findByType() {
        List<Order> list = orderService.findByType("1");
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void list() {
        List<Order> list = orderService.list();
        Assert.assertEquals(2, list.size());
    }

}
