package com.fu.springbootrabbitmq2.consumer;

import com.fu.springbootrabbitmq2.config.ConfirmConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 *   报警消费者
 */
@Component
public class WarningConsumer {
    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void getWarningQueue(Message message){
        System.out.println("报警消费者已经接收到报警 ： -》"+new String(message.getBody()));
    }
}
