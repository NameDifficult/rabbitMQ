package hello;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 接收消息
 */
public class Consumer {
    //队列名称
    public static final String QUEUE_NAME="hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //ip
        connectionFactory.setHost("192.168.78.132");
        //用户名
        connectionFactory.setUsername("admin");
        //密码
        connectionFactory.setPassword("123");
        //创建链接
        Connection connection = connectionFactory.newConnection();
        //根据连接获取信道
        Channel channel = connection.createChannel();

        //声明 接收消息
        DeliverCallback deliverCallback = ((consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        });
        // 取消消息的回调
        CancelCallback cancelCallback = consumerTag ->{
            System.out.println("消息消费被中断");
        };

        /*
         *  接收消息
         *  1. 队列名称
         *  2. 消费成功后是否自动应答  ， false是手动应答
         *  3. 消费成功的回调
         *  4. 消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);

    }
}
