package wordQueueHead;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import utils.RabbitUtils;

/**
 *      手动应答
 * 消息不丢失，可以重新放回队列中
 * 当其中一个接收线程挂了后，会将信息重新放回队列，由队列重新再派发信息到工作线程
 */
public class Work1 {
    public static final String QUEUE_NAME = "admin";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitUtils.getChannel();
        DeliverCallback deliverCallback = (consumerTag,message)->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(new String(message.getBody(),"UTF-8"));
            /*
             *   1. 消息的唯一标记
             *   2. 是否批量应答
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };

        CancelCallback cancelCallback = consumerTag ->{
            System.out.println("接收失败");
        };

        /*
         *  1 ： 设置为不公平分发，不会进行轮询分发，而是发给空闲的接收线程
         *  大于1 ： 预取值    表示预先取多少条信息
         */
        channel.basicQos(1);
        channel.basicConsume(QUEUE_NAME,false,deliverCallback,cancelCallback);
    }
}
