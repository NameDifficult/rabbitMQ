package workQueue;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import utils.RabbitUtils;

public class QueueWord2 {
    public static final String QUEUE_NAME="admin";

    public static void main(String[] args)throws Exception{

        Channel channel = RabbitUtils.getChannel();
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println(new String(message.getBody()));
        };
        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println("消息接收失败"+consumerTag);
        };
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);

    }
}
