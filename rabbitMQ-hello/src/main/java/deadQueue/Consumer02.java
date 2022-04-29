package deadQueue;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import utils.RabbitUtils;

import java.util.HashMap;
import java.util.Map;

public class Consumer02 {
    public static void main(String[] args) throws Exception{

        Channel channel = RabbitUtils.getChannel();
        DeliverCallback deliverCallback = (a,b)->{
            System.out.println("consumer02"+new String(b.getBody()));
        };
        CancelCallback cancelCallback = a->{};
        channel.basicConsume(DeadUtils.DEAD_QUEUE,true,deliverCallback,cancelCallback);
    }

}
