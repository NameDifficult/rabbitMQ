package fanout;

import com.rabbitmq.client.Channel;
import utils.RabbitUtils;

import java.util.Scanner;

public class EmitLog {
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitUtils.getChannel();
        channel.exchangeDeclare("Logs","fanout");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String next = scanner.next();
            channel.basicPublish("Logs","",null,next.getBytes());
            System.out.println("成功发送");
        }
    }
}
