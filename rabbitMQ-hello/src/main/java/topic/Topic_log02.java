package topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import utils.RabbitUtils;

/**
 *  主题交换机
 */
public class Topic_log02 {
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitUtils.getChannel();
        //声明队列和交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        channel.queueDeclare("Q2",false,false,false,null);
        channel.queueBind("Q2",EXCHANGE_NAME,"aa.#");
        channel.queueBind("Q2",EXCHANGE_NAME,"*.*.cc");
        //接收消息
        DeliverCallback deliverCallback = (a,b)->{
            System.out.println(new String(b.getBody()));
        };
        CancelCallback cancelCallback = (a)->{};
        channel.basicConsume("Q2",true,deliverCallback,cancelCallback);
    }
}
