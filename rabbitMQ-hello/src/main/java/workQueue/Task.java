package workQueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import utils.RabbitUtils;

import java.util.Scanner;

/**
 * 发送大量线程
 * 工作队列模式
 */
public class Task {
    public static final String QUEUE_NAME="admin";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        //发送消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            System.out.println("发送完成");
        }

    }
}
