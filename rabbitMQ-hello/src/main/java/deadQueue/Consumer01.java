package deadQueue;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import utils.RabbitUtils;

import java.util.HashMap;
import java.util.Map;

public class Consumer01 {
    public static void main(String[] args) throws Exception{

        Channel channel = RabbitUtils.getChannel();
        //声明死信交换机和普通交换机的类型
        channel.exchangeDeclare(DeadUtils.NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DeadUtils.DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        //设置普通队列的死信参数
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange",DeadUtils.DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key","C2");
        arguments.put("x-max-length",6);//普通队列的长度
        //普通队列
        channel.queueDeclare(DeadUtils.NORMAL_QUEUE,false,false,false,arguments);
        //声明死信队列
        channel.queueDeclare(DeadUtils.DEAD_QUEUE,false,false,false,null);
        //绑定
        channel.queueBind(DeadUtils.NORMAL_QUEUE,DeadUtils.NORMAL_EXCHANGE,"C1");
        channel.queueBind(DeadUtils.DEAD_QUEUE,DeadUtils.DEAD_EXCHANGE,"C2");


        DeliverCallback deliverCallback = (a,b)->{
            String message = new String(b.getBody());
            if ("info8".equals(message)){
                //拒绝接收此信息，并且不塞回队列
                channel.basicReject(b.getEnvelope().getDeliveryTag(),false);
            }else {
                channel.basicAck(b.getEnvelope().getDeliveryTag(),false);
                System.out.println("consumer01"+new String(b.getBody()));
            }
        };
        CancelCallback cancelCallback = a->{};
        channel.basicConsume(DeadUtils.NORMAL_QUEUE,false,deliverCallback,cancelCallback);



    }

}
