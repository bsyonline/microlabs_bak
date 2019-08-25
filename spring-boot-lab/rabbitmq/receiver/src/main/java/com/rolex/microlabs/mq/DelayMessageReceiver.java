package com.rolex.microlabs.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.rolex.microlabs.config.RabbitmqConfig.DELAY_QUEUE;

@Component
@Slf4j
public class DelayMessageReceiver {

    @RabbitListener(queues = DELAY_QUEUE)
    public void receiveDelayMsg(Message message, Channel channel) throws IOException {
        log.info("收到延时消: {}", new String(message.getBody()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}