package wordQueueHead;

import com.rabbitmq.client.Channel;
import utils.RabbitUtils;

import java.util.Scanner;

public class Task {
    public static final String QUEUE_NAME = "admin";
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes("UTF-8"));
            System.out.println("已经发送");
        }
    }
}
