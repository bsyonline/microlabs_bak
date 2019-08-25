/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rolex
 * @since 2019
 */
@Configuration
public class RabbitmqConfig {

    public static final String HELLO_EXCHANGE = "hello-exchange";
    public static final String HELLO_QUEUE = "hello.queue";
    public static final String HELLO_ROUTING_KEY = "#";

    public static final String DEAD_LETTER_EXCHANGE = "hello-dlx";
    public static final String DEAD_LETTER_REDIRECT_ROUTING_KEY = "hello-dl-key";
    public static final String DEAD_LETTER_QUEUE = "hello-dlq";

    //声明队列
    @Bean
    public Queue queue1() {
        Map<String, Object> args = new HashMap<>(2);
        //       x-dead-letter-exchange    声明  死信队列Exchange
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        //       x-dead-letter-routing-key    声明 死信队列抛出异常重定向队列的routingKey(TKEY_R)
        args.put("x-dead-letter-routing-key", DEAD_LETTER_REDIRECT_ROUTING_KEY);
        return QueueBuilder.durable(HELLO_QUEUE).withArguments(args).build();
    }

    //声明交互器
    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(HELLO_EXCHANGE);
    }

    //绑定
    @Bean
    public Binding binding1() {
        return BindingBuilder.bind(queue1()).to(topicExchange()).with(HELLO_ROUTING_KEY);
    }

}
