package chenyunlong.zhangli.mq.kafka;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

public class ProducerCreatorTest {

    @Test
    public void createProducer() {


        String TOPIC = "test-topic";
        Producer<String, String> producer = ProducerCreator.createProducer();
        ProducerRecord<String, String> record =
                new ProducerRecord<>(TOPIC, "hello, Kafka!");
        try {
            //send message
            RecordMetadata metadata = producer.send(record).get();
            System.out.println("Record sent to partition " + metadata.partition()
                    + " with offset " + metadata.offset());
        } catch (ExecutionException |
                InterruptedException e) {
            System.out.println("Error in sending record");
            e.printStackTrace();

            producer.close();
        }
    }
}