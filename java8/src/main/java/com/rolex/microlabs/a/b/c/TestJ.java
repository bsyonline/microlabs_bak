package com.rolex.microlabs.a.b.c;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 保证字符串的打印顺序是："Call B"-->"Call A"-->"execute here"
 *
 * @author rolex
 * @Since 06/11/2019
 */
public class TestJ {
    
    public static void main(String[] args) throws InterruptedException {
        Thread a = new ThreadA();
        Thread b = new ThreadB();
        b.start();
        b.join();
        a.start();
        a.join();
        System.out.println("execute here");
        
        System.out.println("--");

        ScheduledExecutorService scheduleExecutorService = Executors.newScheduledThreadPool(5);
        scheduleExecutorService.schedule(a, 100, TimeUnit.MILLISECONDS);
        scheduleExecutorService.schedule(b, 0, TimeUnit.MILLISECONDS);
        scheduleExecutorService.awaitTermination(3,TimeUnit.SECONDS);
        while (!scheduleExecutorService.isShutdown()) {
            scheduleExecutorService.shutdown();
            System.out.println("shutting down");
        }
        System.out.println("execute here");
    
        System.out.println("--");
        
        a.start();
        a.suspend();
        b.start();
        b.join();
        a.resume();
        a.join();
        System.out.println("execute here");
    }
    
    static class ThreadA extends Thread {
        @Override
        public void run() {
            System.out.println("Call A");
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    static class ThreadB extends Thread {
        @Override
        public void run() {
//            System.out.println("-- B start");
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Call B");
        }
    }
}
