package com.rolex.microlabs.a.b.c;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author rolex
 * @Since 07/11/2019
 */
public class TestL {
    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 100,
            60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(20),
            new ThreadPoolExecutor.AbortPolicy());
    }
}
