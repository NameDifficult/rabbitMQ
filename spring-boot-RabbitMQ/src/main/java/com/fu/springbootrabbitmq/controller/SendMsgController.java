package com.fu.springbootrabbitmq.controller;


import com.fu.springbootrabbitmq.config.DelayedQueueConfig;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  用于发送延迟消息
 */
@RestController
@RequestMapping("/ttl")
public class SendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message){
        System.out.println("发送消息");
        rabbitTemplate.convertAndSend("X","XA","10s你好"+message);
        rabbitTemplate.convertAndSend("X","XB","40s你好"+message);
    }


    //发消息 消息和ttl
    @RequestMapping("/sendMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message,@PathVariable String ttlTime){
        System.out.println("发送消息");
        MessagePostProcessor messagePostProcessor = time->{
            time.getMessageProperties().setExpiration(ttlTime);
            return time;
        };
        rabbitTemplate.convertAndSend("X","XC",message,messagePostProcessor);
    }



    //基于插件
    //发消息延迟消息给延迟交换机
    @RequestMapping("/sendDelayedMsg/{message}/{ttlTime}")
    public void sendDelayedMsg(@PathVariable String message,@PathVariable Integer ttlTime){
        System.out.println("发送消息");
        MessagePostProcessor messagePostProcessor = time->{
            time.getMessageProperties().setDelay(ttlTime);
            return time;
        };
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME,DelayedQueueConfig.DELAYED_ROUTING_KEY,message,messagePostProcessor);
    }
}
