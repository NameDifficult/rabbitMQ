package direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import utils.RabbitUtils;

public class DirectLogs01 {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitUtils.getChannel();
        //路由模式，直接交换机,可以将消息根据routingKey指定发送
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare("console",false,false,false,null);
        channel.queueBind("console",EXCHANGE_NAME,"info");
        channel.queueBind("console",EXCHANGE_NAME,"warning");
        DeliverCallback deliverCallback = (a,b)->{
            System.out.println(new String(b.getBody()));
        };
        CancelCallback cancelCallback = a ->{
            System.out.println("aaa");
        };
        channel.basicConsume("console",true,deliverCallback,cancelCallback);
    }
}
