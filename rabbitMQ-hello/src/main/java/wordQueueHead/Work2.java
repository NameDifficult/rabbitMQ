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
        /*
         *  1 ： 设置为不公平分发，不会进行轮询分发，而是发给空闲的接收线程
         *  大于1 ： 预取值    表示预先取多少条信息
         */
        channel.basicQos(1);
        channel.basicConsume(QUEUE_NAME,false,deliverCallback,cancelCallback);
    }
}
