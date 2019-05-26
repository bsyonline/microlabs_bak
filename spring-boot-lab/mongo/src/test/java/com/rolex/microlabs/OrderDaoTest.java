/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import com.google.common.collect.Lists;
import com.rolex.microlabs.dao.OrderDao;
import com.rolex.microlabs.model.Contact;
import com.rolex.microlabs.model.Location;
import com.rolex.microlabs.model.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

/**
 * @author rolex
 * @since 2019
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class OrderDaoTest {

    @Autowired
    OrderDao orderDao;

    @Before
    public void reset() {
        String[] name = {"alice", "bob", "tom", "jack", "jim"};
        String[] phone = {"18600001111", "18600002345", "18600006789", "18600001357", "18600002468"};
        String[] coffee = {"Americano", "Latte", "Macchiato", "Cappuccino", "Mocha"};
        orderDao.remove();

        Order order = new Order();
        order.setCustomerId(new Random().nextInt(5));
        order.setUserId(new Random().nextInt(5));
        order.setName(coffee[new Random().nextInt(5)]);
        order.setIdt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        order.setUdt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        order.setContact(new Contact(phone[new Random().nextInt(5)], name[new Random().nextInt(5)] + "@gmail.com", new Location(new Random().nextFloat() * 100, new Random().nextFloat() * 100)));
        orderDao.save(order);

        List list = Lists.newArrayList();
        for (int i = 0; i < 20; i++) {
            Order order1 = new Order();
            order1.setCustomerId(new Random().nextInt(5));
            order1.setUserId(new Random().nextInt(5));
            order1.setName(coffee[new Random().nextInt(5)]);
            order1.setIdt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            order1.setUdt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            order1.setContact(new Contact("18600001111", "tom@gmail.com", new Location(new Random().nextFloat() * 100, new Random().nextFloat() * 100)));
            list.add(order1);
        }
        orderDao.save(list);
    }

    @Test
    public void sortByUserDesc() {
        orderDao.sortByUserDesc();
    }

    @Test
    public void groupByCustomer() {
        orderDao.groupByUser();
    }

    @Test
    public void groupByCustomerAndUser() {
        orderDao.groupByCustomerAndUser();
    }

    @Test
    public void countByCustomerDistinctByUser() {
        orderDao.findByCustomerDistinctByUser(1);
    }

    @Test
    public void countByCustomerDistinctByUser1() {
        orderDao.findByCustomerDistinctByUser1(1);
    }

    @Test
    public void findAll() {
        long total = orderDao.count();
        int page = 0;
        int pageSize = 4;
        long totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        int count = 0;
        while (page < totalPage) {
            List<Order> list = orderDao.findAll(page, pageSize);
            if (!list.isEmpty()) {
                for (Order order1 : list) {
                    System.out.println(count + "==" + order1);
                    count++;
                }
            }
            page++;
        }

    }

    @Test
    public void findAll1() {
        long total = orderDao.count();
        int count = 0;
        String head = "5ce894bab968ff0e28e8b1d8";
        int batchSize = 2;
        int limit = 4;

        while (count < total - 1) {
            List<Order> list = orderDao.findAll(head, batchSize, limit);
            if (!list.isEmpty()) {
                for (Order order1 : list) {
                    System.out.println(count + "==" + order1);
                    count++;
                }
                Order order = list.get(list.size() - 1);
                System.out.println("=========" + order);
                head = order.getObjectId();
            }
        }

    }
}
