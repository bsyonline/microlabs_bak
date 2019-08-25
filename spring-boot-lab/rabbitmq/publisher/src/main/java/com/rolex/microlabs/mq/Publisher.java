/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.mq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

import static com.rolex.microlabs.config.RabbitmqConfig.HELLO_EXCHANGE;
import static com.rolex.microlabs.config.RabbitmqConfig.HELLO_ROUTING_KEY;

/**
 * @author rolex
 * @since 2019
 */
@Component
public class Publisher implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            System.out.println("收到ACK,消息发送成功:" + correlationData);
        } else {
            System.out.println("消息发送失败:" + cause);
        }
    }

    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        // confirm 只能保证消息到达broker，但是是否到达queue不能确定，所以需要记录return message
        // 如果消息没有投递到queue，消息会被退回
        System.out.println("消费退回 : " + message.getMessageProperties().getCorrelationId());
    }

    public void send(String msg) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        System.out.println("开始发送消息 : " + msg.toLowerCase());
        rabbitTemplate.convertAndSend(HELLO_EXCHANGE, HELLO_ROUTING_KEY, msg, correlationId);
        System.out.println("结束发送消息 : " + msg.toLowerCase());
    }
}
