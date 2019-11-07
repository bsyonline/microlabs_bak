package com.rolex.microlabs.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author rolex
 * @Since 07/11/2019
 */
public class BlockingQueueTest {
    
    public static void main(String[] args) {
        LinkedBlockingDeque<Integer> blockingDeque = new LinkedBlockingDeque<>(3);
        boolean flag1 = blockingDeque.add(1);
        System.out.println("添加元素：" + flag1);
        System.out.println(blockingDeque.size());
        list(blockingDeque);
        
        boolean flag2 = blockingDeque.offer(2);
        System.out.println("添加元素：" + flag2);
        boolean flag3 = blockingDeque.offer(3);
        System.out.println("添加元素：" + flag3);
        boolean flag4 = blockingDeque.offer(4);
        System.out.println("添加元素：" + flag4);
        System.out.println(blockingDeque.size());
        list(blockingDeque);
    
        /*try {
            blockingDeque.put(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(blockingDeque.size());
        list(blockingDeque);*/
        
        Integer i = blockingDeque.peek();
        System.out.println("检查元素：" + i);
        System.out.println(blockingDeque.size());
        list(blockingDeque);
        
        Integer e = blockingDeque.poll();
        System.out.println("取出元素：" + e);
        System.out.println(blockingDeque.size());
        list(blockingDeque);
        
        blockingDeque.poll();
        blockingDeque.poll();
        Integer e1 = blockingDeque.poll();
        System.out.println("取出元素：" + e1);
        System.out.println(blockingDeque.size());
        list(blockingDeque);
    }
    
    public static void list(BlockingQueue queue) {
        System.out.print("queue的元素： ");
        queue.forEach(e -> {
            System.out.print(e + " ");
        });
        System.out.println();
    }
}
