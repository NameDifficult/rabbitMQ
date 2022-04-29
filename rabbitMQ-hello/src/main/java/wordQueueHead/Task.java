package wordQueueHead;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;
import utils.RabbitUtils;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class Task {
    public static final String QUEUE_NAME = "admin";
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitUtils.getChannel();
        /*
         *  开启发布确认，此功能能确认队列跟消息是否持久化到硬盘了
         *      1. 单个确认模式       ：消息被确认后再发第二条
         *      2. 批量确认          ：发布了n条后再确认
         *      3. 异步确认
         */
        channel.confirmSelect();

        //将队列持久化到硬盘
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            //将消息持久化到硬盘
            channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes("UTF-8"));
            //进行发布确认判断
            if (channel.waitForConfirms()){
                System.out.println("已经发送");
            }
        }
    }


    /**
     * 异步确认
     *      效率高
     *      可以一直发布，发布确认交给接口统计
     */
    public static void publishMessageAsync() throws Exception{
        Channel channel = RabbitUtils.getChannel();
        channel.confirmSelect();
        String queryName = UUID.randomUUID().toString();
        channel.queueDeclare(queryName,true,false,false,null);

        /**
         * 开启一个线程，是一个安全有序的哈希表，适用于高并发的情况
         * 1. 能够将消息编号与消息体关联
         * 2. 轻松批量删除
         * 3. 支持高并发
         */
        ConcurrentSkipListMap<Long,String> concurrentSkipListMap = new ConcurrentSkipListMap<>();


        //参数1. 消息的唯一标识
        //参数2. 是否为批量确认
        //消息确认成功 回调
        ConfirmCallback ackCallback = (deliveryTag,multiple)->{
            //拿到被确认的消息,并查看是否是批量
            if (!multiple){
                //单个删除
                concurrentSkipListMap.remove(deliveryTag);
            }else {
                //批量删除
                ConcurrentNavigableMap<Long, String> map = concurrentSkipListMap.headMap(deliveryTag);
                map.clear();
            }
            System.out.println("确认的消息"+deliveryTag);
        };
        //消息确认失败 回调
        ConfirmCallback nackCallback = (deliveryTag,multiple)->{
            System.out.println("失败的消息"+concurrentSkipListMap.get(deliveryTag));
        };
        //异步确认监听
        channel.addConfirmListener(ackCallback,nackCallback);
        //批量发送
        for (int i = 0 ; i < 1000 ; i++){
            String s = i + "";
            channel.basicPublish("",queryName,null,s.getBytes());
            concurrentSkipListMap.put(channel.getNextPublishSeqNo(),s);
        }

    }
}
