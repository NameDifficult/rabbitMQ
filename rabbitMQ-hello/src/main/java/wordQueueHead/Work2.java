package wordQueueHead;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import utils.RabbitUtils;

public class Work2 {
    public static final String QUEUE_NAME = "admin";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitUtils.getChannel();
        DeliverCallback deliverCallback = (consumerTag, message)->{
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(new String(message.getBody(),"UTF-8"));
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        CancelCallback cancelCallback = consumerTag->{
            System.out.println("接收消息失败");
        };
        channel.basicConsume(QUEUE_NAME,false,deliverCallback,cancelCallback);
    }
}
