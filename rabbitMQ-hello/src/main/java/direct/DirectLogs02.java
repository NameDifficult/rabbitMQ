package direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import utils.RabbitUtils;

public class DirectLogs02 {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare("disk",false,false,false,null);
        channel.queueBind("disk",EXCHANGE_NAME,"error");
        DeliverCallback deliverCallback = (a,b)->{
            System.out.println(new String(b.getBody()));
        };
        CancelCallback cancelCallback = a ->{
            System.out.println("aaa");
        };
        channel.basicConsume("disk",true,deliverCallback,cancelCallback);
    }
}
