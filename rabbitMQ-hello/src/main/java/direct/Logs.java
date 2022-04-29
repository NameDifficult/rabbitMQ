package direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import utils.RabbitUtils;

import java.util.Scanner;

public class Logs {
    public static final String EXCHANGE_NAME = "direct_logs";
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String next = scanner.next();
            //指定发送
            channel.basicPublish(EXCHANGE_NAME,"warning",null,next.getBytes());
            System.out.println("成功发送");
        }
    }
}
