/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.lock;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

/**
 * @author rolex
 * @since 2018
 */
public class DistributedLockTest {
    private static final String LOCK_PATH = "/test/lock";
    Logger logger = LoggerFactory.getLogger(getClass());
    private ZkClient zkClient;

    @Before
    public void mySetUp() throws Exception {
        zkClient = createClient();
    }

    @Test
    public void testFailDoubleLock() {
        SimpleDistributedLock lock = new SimpleDistributedLock( LOCK_PATH);
        lock.lock();
        try {
            lock.lock();
            fail("Exception expected!");
        } catch (Exception e) {
            // expected
        } finally {
            lock.unlock();
        }
    }

    @Test
    public void testFailUnlock() {
        SimpleDistributedLock lock = new SimpleDistributedLock( LOCK_PATH);
        try {
            lock.unlock();
            fail("Expected exception while trying to unlock!");
        } catch (Exception e) {
            // success
        }
    }

    @Test
    public void testTwoLocks() {
        SimpleDistributedLock lock1 = new SimpleDistributedLock( LOCK_PATH);
        SimpleDistributedLock lock2 = new SimpleDistributedLock( LOCK_PATH);
        lock1.lock();
        List<String> children = expectZkNodes(LOCK_PATH);
        assertEquals("One child == lock held!", children.size(), 1);
        lock1.unlock();
        // check no locks held/empty children
        children = expectZkNodes(LOCK_PATH);
        assertEquals("No children, no lock held!", children.size(), 0);
        lock2.lock();
        children = expectZkNodes(LOCK_PATH);
        assertEquals("One child == lock held!", children.size(), 1);
        lock2.unlock();
    }

    @Test
    public void testTwoLocksFailFast() throws InterruptedException {
        SimpleDistributedLock lock1 = new SimpleDistributedLock( LOCK_PATH);
        SimpleDistributedLock lock2 = new SimpleDistributedLock( LOCK_PATH);
        lock1.lock();
        boolean acquired = lock2.tryLock(10, TimeUnit.MILLISECONDS);
        if(acquired){
            logger.info("lock2 holds lock, and do work");
        }
        assertFalse("Couldn't acquire lock because it's currently held", acquired);
        lock1.unlock();
        lock2.unlock();
    }

    @Test
    @Ignore("pending: <http://jira.local.twitter.com/browse/RESEARCH-49>")
    public void testMultiConcurrentLocking() throws Exception {
        //TODO(Florian Leibert): this is a bit janky, so let's replace it.
        for (int i = 0; i < 50; i++) {
            testConcurrentLocking();
        }
        mySetUp();
    }

    @Test
    public void testConcurrentLocking() throws Exception {
        ZkClient zk1 = createClient();
        ZkClient zk2 = createClient();
        ZkClient zk3 = createClient();

        final SimpleDistributedLock lock1 = new SimpleDistributedLock( LOCK_PATH);
        final SimpleDistributedLock lock2 = new SimpleDistributedLock( LOCK_PATH);
        final SimpleDistributedLock lock3 = new SimpleDistributedLock( LOCK_PATH);
        Callable<Object> t1 = new Callable<Object>() {
            @Override
            public Object call() throws InterruptedException {
                lock1.lock();
                try {
                    logger.info("t1 do work");
                    Thread.sleep(50);
                } finally {
                    lock1.unlock();
                }
                return new Object();
            }
        };

        Callable<Object> t2 = new Callable<Object>() {
            @Override
            public Object call() throws InterruptedException {
                System.out.println("call lock2.lock");
                lock2.lock();
                try {
                    logger.info("t2 do work");
                    Thread.sleep(50);
                } finally {
                    lock2.unlock();
                }
                return new Object();
            }
        };

        Callable<Object> t3 = new Callable<Object>() {
            @Override
            public Object call() throws InterruptedException {
                System.out.println("call lock3.lock");
                lock3.lock();
                try {
                    logger.info("t3 do work");
                    Thread.sleep(50);
                } finally {
                    lock3.unlock();
                }
                return new Object();
            }
        };

        ExecutorService ex = Executors.newCachedThreadPool();
        @SuppressWarnings("unchecked") List<Callable<Object>> tlist = Arrays.asList(t1, t2, t3);
        ex.invokeAll(tlist);
        assertTrue("No Children left!", expectZkNodes(LOCK_PATH).size() == 0);
    }

    private ZkClient createClient() {
        return new ZkClient("localhost:2181",
                10000,
                10000,
                new SerializableSerializer());
    }

    protected List<String> expectZkNodes(String path) {
        try {
            List<String> children = zkClient.getChildren(path);
            return children;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}