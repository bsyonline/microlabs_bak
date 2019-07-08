/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import com.google.common.base.Preconditions;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author rolex
 * @since 2019
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ZkTest {

    ZkClient zkClient;

    @Before
    public void setup(){
        zkClient = new ZkClient("localhost:2181");
    }

    @Test
    public void testCreatePath(){
        String path = "/lock/test_zk_lock";
        createNode(path);

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createNode(String path) {
        Preconditions.checkNotNull(path);
        Preconditions.checkArgument(StringUtils.startsWith(path, "/"), "path can be start with '/'");
        if (!zkClient.exists(path)) {
            int lastIndex = path.lastIndexOf("/");
            if (lastIndex > 0) {
                String parent = path.substring(0, lastIndex);
                createNode(parent);
            }
            zkClient.createPersistent(path);
        }
    }
}
