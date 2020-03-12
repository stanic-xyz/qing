package chenyunlong.zhangli.mq.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.Test;

import java.time.Duration;
import java.util.Collections;

import static org.junit.Assert.*;

public class ConsumerCreatorTest {

    @Test
    public void createConsumer() {

        String TOPIC = "test-topic";
        Consumer<String, String> consumer = ConsumerCreator.createConsumer();
        // 循环消费消息
        while (true) {
            //subscribe topic and consume message订阅消费者消息
            consumer.subscribe(Collections.singletonList(TOPIC));
            //接受来自生产者的消息
            ConsumerRecords<String, String> consumerRecords =
                    consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                System.out.println("Consumer consume message:" + consumerRecord.value());
            }
        }
    }
}