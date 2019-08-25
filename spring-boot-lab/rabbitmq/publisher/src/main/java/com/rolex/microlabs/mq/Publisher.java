/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

import static com.rolex.microlabs.config.RabbitmqConfig.*;

/**
 * @author rolex
 * @since 2019
 */
@Component
@Slf4j
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
            log.info("收到ACK,消息发送成功: {}", correlationData);
        } else {
            log.error("消息发送失败: {}", cause);
        }
    }

    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        // confirm 只能保证消息到达broker，但是是否到达queue不能确定，所以需要记录return message
        // 如果消息没有投递到queue，消息会被退回
        log.error("消费退回: {}", message.getMessageProperties().getCorrelationId());
    }

    public void sendDeadLetterMsg(String msg) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        log.info("开始发送消息: {}", msg.toLowerCase());
        rabbitTemplate.convertAndSend(HELLO_EXCHANGE, HELLO_ROUTING_KEY, msg, correlationId);
        log.info("结束发送消息: {}", msg.toLowerCase());
    }

    public void sendDelayMsg(String msg) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        log.info("开始发送延时消息: {}", msg.toLowerCase());
        rabbitTemplate.convertAndSend(WORLD_EXCHANGE, WORLD_ROUTING_KEY, msg, correlationId);
        log.info("结束发送延时消息: {}", msg.toLowerCase());
    }
}
