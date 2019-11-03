/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.pool;

import java.util.concurrent.*;

/**
 * @author rolex
 * @since 2019
 */
public class ThreadPoolDemo {

    public void test1() throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 5,
                3, TimeUnit.SECONDS, new LinkedBlockingQueue<>(Integer.MAX_VALUE));
        execute(executor);
    }

    public void test2() throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 5,
                3, TimeUnit.SECONDS, new LinkedBlockingDeque<>(10), new ThreadPoolExecutor.DiscardPolicy());
        execute(executor);
    }

    public void test3() throws InterruptedException {
        Executor executor = Executors.newFixedThreadPool(5);
        //new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        execute(executor);
    }

    public void test4() throws InterruptedException {
        Executor executor = Executors.newSingleThreadExecutor();
        //new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()));
        execute(executor);
    }

    public void test5() throws InterruptedException {
        Executor executor = Executors.newCachedThreadPool();
        //new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        execute(executor);
    }

    private void execute(Executor executor) throws InterruptedException {
        for (int i = 0; i < 25; i++) {
            int n = i + 1;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("开始执行：" + n);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("执行结束：" + n);
                }
            });
            System.out.println("任务提交：" + n);
            System.out.println("当前线程池线程数量为：" + ((ThreadPoolExecutor) executor).getPoolSize());
            System.out.println("当前线程池等待线程数量为：" + ((ThreadPoolExecutor) executor).getQueue().size());
        }
        Thread.sleep(500);
        System.out.println("----------------");
        System.out.println("所有任务提交完成");
        System.out.println("当前线程池线程数量为：" + ((ThreadPoolExecutor) executor).getPoolSize());
        System.out.println("当前线程池等待线程数量为：" + ((ThreadPoolExecutor) executor).getQueue().size());
        // 等待15秒
        Thread.sleep(15000);
        System.out.println("当前线程池线程数量为：" + ((ThreadPoolExecutor) executor).getPoolSize());
        System.out.println("当前线程池等待线程数量为：" + ((ThreadPoolExecutor) executor).getQueue().size());
    }


    public static void main(String[] args) throws InterruptedException {
        ThreadPoolDemo demo = new ThreadPoolDemo();
        demo.test1();
//        demo.test2();
//        demo.test3();
//        demo.test4();
//        demo.test5();
    }
}
