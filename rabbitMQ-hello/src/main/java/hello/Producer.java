package hello;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者
 * 发送消息
 */
public class Producer {
    //队列名称
    public static final String QUEUE_NAME="hello";

    //用于发消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //工程ip ，  连接rabbitMQ的队列
        connectionFactory.setHost("192.168.78.132");
        //用户名
        connectionFactory.setUsername("admin");
        //设置密码
        connectionFactory.setPassword("123");
        //创建连接,连接里有很多信道
        Connection connection = connectionFactory.newConnection();
        //创建信道，用信道发消息
        Channel channel = connection.createChannel();
        /*
         *  生成一个队列
         *      1. 队列名称
         *      2. 队列里面的消息是否持久化到硬盘，默认情况存储在内存
         *      3. 该队列是否只允许一个消费者使用， 就是是否进行消息共享，
         *      4. 是否自动删除
         *      5. 其他参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //发消息
        String massage = "你好";
        /*
         *  消息内容
         *  1. 交换机
         *  2. 路由的key，本次是队列名称
         *  3. 其他参数
         *  4. 消息体
         */
        channel.basicPublish("",QUEUE_NAME,null,massage.getBytes());
        System.out.println("发送完毕");

    }
}
