package com.fu.springbootrabbitmq2.controller;

import com.fu.springbootrabbitmq2.config.ConfirmConfig;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SendMsgController {

    @Autowired
    private RabbitTemplate template;

    @RequestMapping("/confirm/{message}")
    @ResponseBody
    public void confirm(@PathVariable String message){
        System.out.println("发送消息");
        CorrelationData correlationData = new CorrelationData("1");
        template.convertAndSend(ConfirmConfig.EXCHANGE_NAME_CONFIRM,ConfirmConfig.ROUTING_KEY_CONFIRM+"1",message,correlationData);
        //测试交换机名字不正确，会进行回调
//        template.convertAndSend(ConfirmConfig.EXCHANGE_NAME_CONFIRM+"123",ConfirmConfig.ROUTING_KEY_CONFIRM,message,correlationData);
    }



}
