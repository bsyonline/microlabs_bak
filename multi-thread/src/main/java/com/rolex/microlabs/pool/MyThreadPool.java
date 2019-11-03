/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.pool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author rolex
 * @since 2019
 */
public class MyThreadPool {

    private BlockingDeque<Runnable> blockingDeque; // 存放等待线程

    private List<Thread> workers; // 工作队列

    private volatile boolean isWorking = true;

    public MyThreadPool(int poolSize, int taskSize) throws IllegalAccessException {
        if (poolSize <= 0 || taskSize <= 0) {
            throw new IllegalAccessException();
        }
        this.blockingDeque = new LinkedBlockingDeque<>();
        this.workers = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < poolSize; i++) {
            // 启动一个线程并加入到队列
            Worker worker = new Worker(this);
            worker.start();
            workers.add(worker);
        }
    }

    public boolean submit(Runnable runnable) {
        if (isWorking) {
            return this.blockingDeque.offer(runnable); // 往等待队列中插入一个任务
        } else {
            return false;
        }
    }

    /**
     * 关闭线程池：
     * 1. 不能再往任务队列中添加
     * 2. 如果任务队列中还有任务，应该继续执行，但是不阻塞
     * 3. 如果有任务阻塞，则中断任务
     */
    public void shutdown() {
        this.isWorking = false;
        for (Thread thread : workers) {
            if (Thread.State.BLOCKED.equals(thread.getState())) {
                thread.interrupt();
            }
        }
    }

    public static class Worker extends Thread {
        private MyThreadPool pool;

        public Worker(MyThreadPool pool) {
            this.pool = pool;
        }

        @Override
        public void run() {
            while (this.pool.blockingDeque.size() > 0 || this.pool.isWorking) {
                Runnable task = null;
                try {
                    if (this.pool.isWorking) {
                        task = this.pool.blockingDeque.take(); // 如果有任务就移除，没有就阻塞
                    } else {
                        task = this.pool.blockingDeque.poll(); // 移除任务
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (task != null) {
                    task.run();
                    System.out.println("线程" + Thread.currentThread().getName() + "开始执行");
                }
            }
        }
    }

    public static void main(String[] args) throws IllegalAccessException {
        MyThreadPool pool = new MyThreadPool(3, 6);
        for (int i = 0; i < 6; i++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("放入线程");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        System.out.println("线程唤醒");
                    }
                }
            });
        }
        pool.shutdown();
    }
}
