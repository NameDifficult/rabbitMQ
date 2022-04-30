package com.fu.springbootrabbitmq2.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  配置类    可以进行发布确认
 *  发布确认
 *  如果配置了备份交换机，则不会进行消息的回退。将不被接收的消息给备份交换机
 */
@Configuration
public class ConfirmConfig {
    //交换机
    public static final String EXCHANGE_NAME_CONFIRM = "confirm_exchange";
    //队列
    public static final String QUEUE_NAME_CONFIRM = "confirm_queue";
    //routingKey
    public static final String ROUTING_KEY_CONFIRM = "routing-key_confirm";
    //备份交换机
    public static final String BACKUP_EXCHANGE= "backup_exchange";
    //备份队列
    public static final String BACKUP_QUEUE_NAME = "backup_queue";
    //报警队列
    public static final String WARNING_QUEUE_NAME = "warning_queue";


    //声明交换机
    //无法将消息发送给队列时，发送给备份交换机
    @Bean
    public DirectExchange confirmExchange(){

        return ExchangeBuilder.directExchange(EXCHANGE_NAME_CONFIRM).durable(true).withArgument("alternate-exchange",BACKUP_EXCHANGE).build();
    }

    //声明队列
    @Bean
    public Queue confirmQueue(){
        return QueueBuilder.durable(QUEUE_NAME_CONFIRM).build();
    }

    //绑定
    @Bean
    public Binding confirmBinding(@Qualifier("confirmQueue")Queue queue , @Qualifier("confirmExchange")DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_CONFIRM);
    }





    //声明备份交换机 也是扇出交换机
    @Bean
    public FanoutExchange backupExchange(){
        return new FanoutExchange(BACKUP_EXCHANGE);
    }

    //声明备份队列
    @Bean
    public Queue backupQueue(){
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }
    //声明报警队列
    @Bean
    public Queue warningQueue(){
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    //备份队列绑定到备份交换机
    @Bean
    public Binding bindingBackupQueueToBackupExchange(@Qualifier("backupQueue")Queue queue , @Qualifier("backupExchange")FanoutExchange exchange){
        return BindingBuilder.bind(queue).to(exchange);
    }
    //报警队列绑定到备份交换机
    @Bean
    public Binding bindingWarningQueueToBackupExchange(@Qualifier("warningQueue")Queue queue , @Qualifier("backupExchange")FanoutExchange exchange){
        return BindingBuilder.bind(queue).to(exchange);
    }

}
