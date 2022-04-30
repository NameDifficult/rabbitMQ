package com.fu.springbootrabbitmq.config;



import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 *   利用过期时间制作延迟队列
 */
@Configuration
public class TTLQueueConfig {
    //普通交换机
    public static final String X_NORMAL_EXCHANGE = "X";
    //死信交换机
    public static final String Y_DEAD_EXCHANGE = "Y";
    //普通队列1
    public static final String XA_NORMAL_QUEUE_NAME = "QA";
    //普通队列2
    public static final String XB_NORMAL_QUEUE_NAME = "QB";
    //死信队列
    public static final String QD_DEAD_QUEUE_NAME = "QD";

    //指定延迟时间的队列
    public static final String QUEUE_QC = "QC";



    /*
     * 声明交换机
     */
    @Bean("xExchange")
    public DirectExchange xExchange(){
        return new DirectExchange(X_NORMAL_EXCHANGE);
    }
    @Bean("yDeanExchange")
    public DirectExchange yDeanExchange(){
        return new DirectExchange(Y_DEAD_EXCHANGE);
    }


    /*
     *  声明队列
     */
    @Bean("queueXA")
    public Queue queueXA(){
        Map<String,Object> arguments = new HashMap<>(3);
        arguments.put("x-dead-letter-exchange",Y_DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key","YD");
        arguments.put("x-message-ttl",10000);
        return QueueBuilder.durable(XA_NORMAL_QUEUE_NAME).withArguments(arguments).build();
    }
    @Bean("queueXB")
    public Queue queueXB(){
        Map<String,Object> arguments = new HashMap<>(3);
        arguments.put("x-dead-letter-exchange",Y_DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key","YD");
        arguments.put("x-message-ttl",40000);
        return QueueBuilder.durable(XB_NORMAL_QUEUE_NAME).withArguments(arguments).build();
    }
    @Bean("queueDeadQD")
    public Queue queueDeadQD(){
        return QueueBuilder.durable(QD_DEAD_QUEUE_NAME).build();
    }

    @Bean("queueC")
    public Queue queueC(){
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange",Y_DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key","YD");
        return QueueBuilder.durable(QUEUE_QC).withArguments(arguments).build();
    }



    /*
     *  绑定
     * 1. XA队列绑定X交换机
     * 2. XB队列绑定X交换机
     * 3. QD队列绑定Y交换机
     */
    @Bean
    public Binding queueXABindingX(@Qualifier("queueXA") Queue queueA , @Qualifier("xExchange") DirectExchange exchange){
        return BindingBuilder.bind(queueA).to(exchange).with("XA");
    }
    @Bean
    public Binding queueXBBindingX(@Qualifier("queueXB") Queue queueB , @Qualifier("xExchange") DirectExchange exchange){
        return BindingBuilder.bind(queueB).to(exchange).with("XB");
    }
    @Bean
    public Binding queueQDBindingY(@Qualifier("queueDeadQD") Queue queueDeadQD , @Qualifier("yDeanExchange") DirectExchange yDeanExchange){
        return BindingBuilder.bind(queueDeadQD).to(yDeanExchange).with("YD");
    }

    @Bean
    public Binding queueQCBindingX(@Qualifier("queueC") Queue queueC,@Qualifier("xExchange")DirectExchange exchange){
        return BindingBuilder.bind(queueC).to(exchange).with("XC");
    }

}
