/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.cas;

import sun.misc.Unsafe;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author rolex
 * @since 2019
 */
public class CasDemo {

    AtomicInteger atomicInteger = new AtomicInteger();
    static Unsafe unsafe;
    static int offset;
    volatile int i = 0;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
            offset = (int) unsafe.objectFieldOffset(CasDemo.class.getDeclaredField("i"));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void add() {
        i++;
    }

    public void add1() {
        int current;
        int b;
        do {
            current = i;
            b = current + 1;
        } while (!unsafe.compareAndSwapInt(this, offset, current, b));
    }

    public void add2() {
        i = atomicInteger.incrementAndGet();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        CasDemo cas = new CasDemo();
        for (int i = 0; i < 2; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 1000; j++) {
                        cas.add2();
                    }
                }
            }).start();
        }
        Thread.sleep(1000);
        System.out.println(cas.i);
    }

}
