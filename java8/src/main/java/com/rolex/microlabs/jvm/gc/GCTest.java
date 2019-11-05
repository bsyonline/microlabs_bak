package com.rolex.microlabs.jvm.gc;

import java.util.*;

/**
 *
 * -Xms30m -Xmx30m -Xmn10m  -XX:+PrintGCDetails
 *
 * @author rolex
 * @Since 29/10/2019
 */
public class GCTest {
    
    public static void main(String[] args) throws Exception {
        List caches = new ArrayList();
        for (int i = 0; i < 7; i++) {
            System.out.println("第" + (i + 1)+"次Put");
            caches.add(new byte[1024 * 1024 * 3]);
        }
        caches.clear();
        for (int i = 0; i < 2; i++) {
            System.out.println("第" + (i + 8)+"次Put");
            caches.add(new byte[1024 * 1024 * 3]);
        }
    }
    
}