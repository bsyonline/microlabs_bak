package com.rolex.microlabs.mq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;

import static com.rolex.microlabs.config.RabbitmqConfig.DEAD_LETTER_QUEUE;

//@Component
public class DeadLetterMessageReceiver {

    @RabbitListener(queues = DEAD_LETTER_QUEUE)
    public void receiveA(Message message, Channel channel) throws IOException {
        System.out.println("收到死信消息：" + new String(message.getBody()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}