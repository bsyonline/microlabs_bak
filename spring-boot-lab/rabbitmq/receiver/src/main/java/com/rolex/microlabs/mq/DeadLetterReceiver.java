/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.rolex.microlabs.config.RabbitmqConfig.HELLO_QUEUE;

/**
 * @author rolex
 * @since 2019
 */
@Component
@Slf4j
public class DeadLetterReceiver {
    int count = 0;

    @RabbitListener(queues = HELLO_QUEUE)
    public void processMessage(String msg, Channel channel, Message message) {
        count++;
        log.info("{}接收到来自{}队列的消息：{}", Thread.currentThread().getName(), HELLO_QUEUE, msg);
        try {
            if (count % 3 == 0) {
                throw new RuntimeException("test exception");
            }
            // 告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了
            // 否则消息服务器以为这条消息没处理掉 后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("消费成功，返回ACK");
        } catch (Exception e) {
            count--;
            //丢弃这条消息
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
//                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            throw new RuntimeException("消费失败:" + e.getMessage());
        }
    }

}
