package com.fu.springbootrabbitmq.controller;

import com.fu.springbootrabbitmq.config.DelayedQueueConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 *  基于插件
 *  接收延迟消息
 */
@Component
public class DelayedConsumer {
    //监听消息
    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void delayedListen(Message message){
        System.out.println(new String(message.getBody()));
    }
}
