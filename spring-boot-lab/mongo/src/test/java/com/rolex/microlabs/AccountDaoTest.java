/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import com.rolex.microlabs.dao.AccountDao;
import org.junit.Before;
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
public class AccountDaoTest {

    @Autowired
    AccountDao accountDao;

    @Before
    public void setup() {
        accountDao.insert(1, 100);
        accountDao.insert(2, 100);
    }

    @Test
    public void transfer() {
        accountDao.transfer(1, 2, 200);
    }
}
