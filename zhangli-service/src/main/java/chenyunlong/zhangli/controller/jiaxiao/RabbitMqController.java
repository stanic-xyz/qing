package chenyunlong.zhangli.controller.jiaxiao;

import chenyunlong.zhangli.mq.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RequestMapping("mq")
@RestController
public class RabbitMqController {
    private final static String QUEUE_NAME = "q_test_01";


    /**
     * 定义一个消息生产者，每访问一次接口，生产一条消息
     *
     * @param message 消息主题
     * @return
     * @throws Exception
     */
    @ApiOperation("生产一条消息")
    @GetMapping("Producer")
    public String sendMq(@RequestParam String message) throws Exception {

        Connection connection = ConnectionUtil.getConnection();

        // 从连接中创建通道
        Channel channel = connection.createChannel();

        // 声明（创建）队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
        //关闭通道和连接
        channel.close();
        connection.close();
        return "send successfully";
    }

    /**
     * 定义一个消费者接口，没访问一次接口，消费一条消息
     *
     * @return
     * @throws Exception
     */
    @ApiOperation("消费一条消息")
    @GetMapping("Consumer")
    public String comsume() throws Exception {
        Connection connection = ConnectionUtil.getConnection();

        String message = null;
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME, true, consumer);
        // 获取消息
        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
        message = new String(delivery.getBody());
        System.out.println(" [x] Received '" + message + "'");
        channel.close();
        connection.close();
        return message;
    }


}
