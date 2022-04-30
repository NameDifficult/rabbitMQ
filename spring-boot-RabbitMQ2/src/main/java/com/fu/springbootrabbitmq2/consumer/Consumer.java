package com.fu.springbootrabbitmq2.consumer;

import com.fu.springbootrabbitmq2.config.ConfirmConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
    @RabbitListener(queues = ConfirmConfig.QUEUE_NAME_CONFIRM)
    public void getConFirmMsg(Message message){
        System.out.println("接收到的消息："+new String(message.getBody()));
    }
}
