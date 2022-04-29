package deadQueue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import utils.RabbitUtils;

/**
 * 生产者
 */
public class Producer {
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitUtils.getChannel();
        /*
         *      死信触发条件
         *      1. 时间超时
         *      2. 队列超出消费端的最大长度
         *      3. 拒绝接收
         */
        //放入basicPublish中
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();
        for (int i = 1; i < 11; i++) {
            String message = "info"+i;
            channel.basicPublish(DeadUtils.NORMAL_EXCHANGE,"C1",properties,message.getBytes());
        }
    }
}
