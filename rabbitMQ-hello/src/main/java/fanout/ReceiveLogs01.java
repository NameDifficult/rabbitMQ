package fanout;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import utils.RabbitUtils;

public class ReceiveLogs01 {
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitUtils.getChannel();
        //声明交换机,发布订阅模式，扇出交换机
        channel.exchangeDeclare("Logs","fanout");
        //生成一个临时队列
        String queue = channel.queueDeclare().getQueue();
        //绑定交换机与队列
        channel.queueBind(queue,"Logs","");
        DeliverCallback deliverCallback = (a,b)->{
            System.out.println(new String(b.getBody()));
        };
        CancelCallback cancelCallback = a ->{
            System.out.println("lll");
        };
        channel.basicConsume(queue,true,deliverCallback,cancelCallback);
    }
}
