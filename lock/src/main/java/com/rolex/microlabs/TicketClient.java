/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs;

import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;

/**
 * @author rolex
 * @since 2019
 */
public class TicketClient {

    public static void main(String[] args) {
        new TicketClient().reduce();
    }

    public void reduce() {
        RestTemplate restTemplate = new RestTemplate();
        CountDownLatch latch = new CountDownLatch(20);
        for (int i = 0; i < 20; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    restTemplate.exchange("http://localhost:8888/tickets", HttpMethod.GET, null, String.class);
                }
            }).start();
            latch.countDown();
        }
    }
}
