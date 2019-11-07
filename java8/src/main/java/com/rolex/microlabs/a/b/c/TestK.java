package com.rolex.microlabs.a.b.c;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 控制顺序的问题,需要所有线程同时运行，而不是按照for的顺序一个一个启动，并且要求All Executed需要在所有线程都执行完毕后才打印
 *
 * @author rolex
 * @Since 07/11/2019
 */
public class TestK {
    
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.await();
                        System.out.println(Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            latch.countDown();
        }
        executor.awaitTermination(1, TimeUnit.SECONDS);
        if(!executor.isShutdown()){
            executor.shutdown();
        }
        System.out.println("All Executed");
    }
}
