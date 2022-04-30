package com.fu.springbootrabbitmq.controller;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 用于接收延迟的消息
 */
@Component
public class DeadLetterQueueConsumer {

    @RabbitListener(queues = "QD")
    public void received(Message message, Channel channel) throws Exception{
        String msg = new String(message.getBody());
        System.out.println("收到"+msg);
    }
}
