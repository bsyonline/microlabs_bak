/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.lock;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.CreateMode;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author rolex
 * @since 2019
 */
@Slf4j
public class ZkDistributedLock implements Lock {
    
    ZkClient zkClient;
    String lockPath;
    ThreadLocal<String> currentNodeThreadLocal = new ThreadLocal<>();
    ThreadLocal<Boolean> hasLockThreadLocal = new ThreadLocal<>();
    CountDownLatch latch;
    
    public ZkDistributedLock(String lockKey) {
        this.zkClient = createZkClient();
        this.lockPath = "/lock/" + lockKey;
        this.latch = new CountDownLatch(1);
        
        hasLockThreadLocal.set(false);
    }
    
    private ZkClient createZkClient() {
        return new ZkClient("localhost:2181");
    }
    
    @Override
    public void lock() {
        //创建锁的上级路径
        createNode(lockPath);
        //在锁路径下创建临时顺序节点
        String currentNode = zkClient.create(lockPath + "/lock_", null, CreateMode.EPHEMERAL_SEQUENTIAL);
        if (currentNode.contains("/")) {
            String currentId = currentNode.substring(currentNode.lastIndexOf("/") + 1);
            currentNodeThreadLocal.set(currentId);
        }
        log.debug("create ephemeral sequential node {}", currentNode);
        tryLock();
    }
    
    @Override
    public void lockInterruptibly() throws InterruptedException {
        
    }
    
    @Override
    public boolean tryLock() {
        //获取lock路径下的所有节点
        List<String> nodes = zkClient.getChildren(lockPath);
        //排序
        ImmutableList<String> sortedNodes = Ordering.natural().immutableSortedCopy(nodes);
        if (sortedNodes.isEmpty()) {
            log.warn("directory '{}' is empty");
        }
        //获取当前节点的索引，如果index最小，则可以获得锁
        int nodeIndex = sortedNodes.indexOf(currentNodeThreadLocal.get());
        if (nodeIndex < 0) {
            log.warn("node '{}' is not exists", currentNodeThreadLocal.get());
        } else if (nodeIndex == 0) {
            hasLockThreadLocal.set(true);
            latch.countDown();
            log.debug("node '{}' 获得锁", currentNodeThreadLocal.get());
            return true;
        } else {
            //如果不是最小节点，添加监听前一个节点的事件，然后阻塞等待
            String prevNode = lockPath + "/" + sortedNodes.get(nodeIndex - 1);
            zkClient.subscribeDataChanges(prevNode, new IZkDataListener() {
                @Override
                public void handleDataChange(String dataPath, Object data) throws Exception {
                    
                }
                
                @Override
                public void handleDataDeleted(String dataPath) throws Exception {
                    log.debug("Data Deleted 事件: prevNode '{}' 被删除,{}获得锁", dataPath, currentNodeThreadLocal.get());
                    hasLockThreadLocal.set(true);
                    latch.countDown();
                }
            });
            log.debug("node '{}'不是最小节点,没有获得锁,监听{},等待{}被删除", currentNodeThreadLocal.get(), prevNode, prevNode);
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("node '{}' 没有获得锁", currentNodeThreadLocal.get());
        }
        
        return false;
    }
    
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }
    
    @Override
    public void unlock() {
        zkClient.delete(lockPath + "/" + currentNodeThreadLocal.get());
        log.debug("删除节点{}", lockPath + "/" + currentNodeThreadLocal.get());
        currentNodeThreadLocal.remove();
        hasLockThreadLocal.set(false);
        latch = new CountDownLatch(1);
    }
    
    @Override
    public Condition newCondition() {
        return null;
    }
    
    /**
     * 创建lock目录，如果不存在递归创建上一级
     *
     * @param path
     */
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
