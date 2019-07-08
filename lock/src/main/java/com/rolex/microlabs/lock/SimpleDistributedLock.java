/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.lock;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import com.rolex.microlabs.util.ZooKeeperUtils;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author rolex
 * @since 2018
 */
public class SimpleDistributedLock implements DistributedLock {

    Logger logger = LoggerFactory.getLogger(getClass());
    private static final String ARG_NOT_BLANK_MSG = "Argument cannot be blank";
    private final AtomicBoolean aborted = new AtomicBoolean(false);
    private final ZkClient zkClient;
    private final String lockPath;
    private CountDownLatch latch;
    private boolean holdsLock = false;
    private String currentNode;
    private String currentId;
    private String watchedNode;

    /**
     * Creates a distributed lock using the given {@code zkClient} to coordinate locking.
     *
     * @param lockPath The path used to manage the lock under.
     */
    public SimpleDistributedLock(String lockPath) {
        Preconditions.checkArgument(!StringUtils.isEmpty(lockPath), ARG_NOT_BLANK_MSG);
        zkClient = createClient();
        this.lockPath = lockPath;
        this.latch = new CountDownLatch(1);
    }

    private synchronized void prepare() {
        ZooKeeperUtils.ensurePath(zkClient, lockPath);
        logger.info("working with locking path:" + lockPath);
        // Create an EPHEMERAL_SEQUENTIAL node.
        currentNode = zkClient.create(lockPath + "/member_", null, CreateMode.EPHEMERAL_SEQUENTIAL);
        if (currentNode.contains("/")) {
            currentId = currentNode.substring(currentNode.lastIndexOf("/") + 1);
        }
        logger.info("received id from zk:" + currentId);
        logger.info("add listener to node: " + currentId);
    }

    public synchronized void checkLock() {
        Preconditions.checkArgument(!StringUtils.isEmpty(currentId), ARG_NOT_BLANK_MSG);
        List<String> candidates = zkClient.getChildren(lockPath);
        ImmutableList<String> sortedMembers = Ordering.natural().immutableSortedCopy(candidates);
        // throw exception if there are no children!
        if (sortedMembers.isEmpty()) {
            throw new LockingException("error, member list is empty!");
        }
        int memberIndex = sortedMembers.indexOf(currentId);
        if (memberIndex < 0) {
            throw new LockingException("node has not existed: " + currentId);
        } else if (memberIndex == 0) {
            logger.info("node {} holds lock", currentId);
            holdsLock = true;
            latch.countDown();
        } else {
            try {
                final String nextLowestNode = sortedMembers.get(memberIndex - 1);
                logger.info("current LockWatcher with ephemeral node [{}], is " +
                        "waiting for [{}] to release lock.", currentId, nextLowestNode);
                watchedNode = String.format("%s/%s", lockPath, nextLowestNode);
                try {
                    // exception when node not exists
                    zkClient.subscribeDataChanges(watchedNode, new IZkDataListener() {
                        public void handleDataDeleted(String dataPath) throws Exception {
                            latch.countDown();
                        }

                        public void handleDataChange(String dataPath, Object data) throws Exception {
                            // ignore
                        }
                    });
                    latch.await();
                } catch (ZkNoNodeException e) {
                    //ignore
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                logger.error("current LockWatcher with ephemeral node {} got exception. " +
                        "trying to cancel lock acquisition.", currentId);
                cancelAttempt();
            }
        }
    }

    private synchronized void cancelAttempt() {
        logger.info("cancelling lock attempt!");
        cleanup();
        // Bubble up failure...
        holdsLock = false;
        latch.countDown();
    }

    private void cleanup() {
        logger.info("cleaning up {}!", currentId);
        Preconditions.checkNotNull(currentId);
        try {
            boolean exists = zkClient.exists(currentNode);
            if (exists) {
                zkClient.delete(currentNode);
            } else {
                logger.warn("called cleanup but nothing to cleanup!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        holdsLock = false;
        aborted.set(false);
        currentId = null;
        currentNode = null;
        latch = new CountDownLatch(1);
    }

    @Override
    public synchronized void unlock() throws LockingException {
        if (currentId == null) {
            throw new LockingException("error, neither attempting to lock nor holding a lock!");
        }
        Preconditions.checkNotNull(currentId);
        // Try aborting!
        if (!holdsLock) {
            aborted.set(true);
            logger.info("not holding lock, aborting acquisition attempt!");
        } else {
            logger.info("node {} release lock and cleaning up this locks ephemeral node.", currentId);
            cleanup();
        }
    }

    public boolean tryLock(long timeout, TimeUnit unit) throws LockingException {
        if (holdsLock) {
            throw new LockingException("Error, already holding a lock. Call unlock first!");
        }
        prepare();
        checkLock();
        try {
            boolean success = latch.await(timeout, unit);
            if (!success) {
                return false;
            }
            if (!holdsLock) {
                throw new LockingException("error, couldn't acquire the lock!");
            }
        } catch (InterruptedException e) {
            cancelAttempt();
            return false;
        }
        return true;
    }

    @Override
    public synchronized void lock() throws LockingException {
        if (holdsLock) {
            throw new LockingException("error, already holding a lock. Call unlock first!");
        }
        try {
            prepare();
            checkLock();
            latch.await();
            if (!holdsLock) {
                logger.error("{} don't holds lock, retry", currentId);
                checkLock();
            }
        } catch (Exception e) {
            e.printStackTrace();
            cancelAttempt();
            throw new LockingException("exception while trying to acquire lock!");
        }
    }

    private ZkClient createClient() {
        return new ZkClient("localhost:2181",
                10000,
                10000,
                new SerializableSerializer());
    }
}
