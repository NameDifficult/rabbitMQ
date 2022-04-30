package com.fu.springbootrabbitmq2.config;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 *      交换机的回调接口
 *   交换机发送失败进行消息的回调给发送者
 *   要在配置文件中进行配置
 *
 *
 *      队列回调接口
 *      当消息没有被队列接收时回调给发送方
 */
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback  {

    //将重写后的接口重新注入回rabbitTemplate
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 交换机回调方法
     * @param correlationData  回调的ID及相关信息
     * @param b 交换机是否收到消息
     * @param s 如果失败，则是失败的原因   . 成功为null
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        String id = correlationData!=null?correlationData.getId():"";
        if (b){
            System.out.println("交换机已经接收-》id为 ： " + id);
        }else {
            System.out.println("交换机已经接收-》id为 ： " + id + "失败 ： " + s);
        }
    }

    /**
     * 队列回调方法
     * @param returnedMessage
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        //回退信息
        String exchange = returnedMessage.getExchange();//回退的交换机
        String message = new String(returnedMessage.getMessage().getBody());//回退的消息内容
        String replyText = returnedMessage.getReplyText();//回退原因
        String routingKey = returnedMessage.getRoutingKey();//路由key
        System.out.println("队列接收失败：交换机为->"+exchange + "消息为-> " + message + "原因为-> " + replyText);
    }
}
