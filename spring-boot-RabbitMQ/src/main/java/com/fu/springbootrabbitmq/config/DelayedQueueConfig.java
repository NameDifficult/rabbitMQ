package com.fu.springbootrabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于插件
 * 声明延迟交换机
 */
@Configuration
public class DelayedQueueConfig {
    //队列
    public static final String DELAYED_QUEUE_NAME = "delayed_queue";
    //延迟交换机
    public static final String DELAYED_EXCHANGE_NAME = "delayed_exchange";
    //routing-key
    public static final String DELAYED_ROUTING_KEY = "delayed_routing-key";


    /**
     * 自定义交换机
     * 此交换机为延迟交换机
     */
    @Bean
    public CustomExchange delayedExchange(){
        /*
         * 1. 交换机的名称
         * 2. 交换机的类型
         * 3. 是否需要持久化
         * 4. 是否需要自动删除
         * 5. 其他参数
         */
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type","direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME,"x-delayed-message",true,false,arguments);
    }


    /*
     * 声明队列
     */
    @Bean
    public Queue delayedQueue(){
        return new Queue(DELAYED_QUEUE_NAME);
    }


    /*
     * 绑定
     */
    @Bean
    public Binding delayedBinding(@Qualifier("delayedQueue") Queue delayedQueue, @Qualifier("delayedExchange") CustomExchange exchange){
        return BindingBuilder.bind(delayedQueue).to(exchange).with(DELAYED_ROUTING_KEY).noargs();
    }


}
