package com.rolex.microlabs.thread.deadlock;

/*******************************************************************************
 * - Copyright (c)  2018  chinadaas.com
 *   @author rolex
 * - File Name: com.rolex.Deadlock
 * - Description:
 *
 *
 * - Function List:
 *
 *
 * - History:
 * Date         Author          Modification
 * 2019/02/19   rolex           Create file
 *******************************************************************************/
public class DeadlockTest {
    
    Object lock1 = new Object();
    Object lock2 = new Object();
    
    public void m1() {
        synchronized (lock1) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            m2();
        }
    }
    
    public void m2() {
        synchronized (lock2) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            m1();
        }
    }
    
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new DeadlockTest().m1();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new DeadlockTest().m2();
            }
        }).start();
        System.out.println("finished");
    }
    
}
