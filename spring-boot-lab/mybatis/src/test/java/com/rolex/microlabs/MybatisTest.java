package com.rolex.microlabs;

import com.rolex.microlabs.dao.UserDao;
import com.rolex.microlabs.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rolex on 08/18/2018.
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class MybatisTest {

    @Autowired
    UserDao userDao;

    @Test
    public void insert() {
        User user = new User();
        user.setName("tom");
        user.setAge(19);
        int count = userDao.save(user);
        System.out.println("insert record count : " + count);
    }

    @Test
    public void bulkInsert() {
        User user = new User();
        user.setName("alice");
        user.setAge(20);
        User user1 = new User();
        user1.setName("jim");
        user1.setAge(21);
        List<User> list = new ArrayList<>();
        list.add(user);
        list.add(user1);
        int count = userDao.batchSave(list);
        System.out.println("insert records count : " + count);
    }

}
