package cn.chenyunlong.qing.service.qmall.config;

import cn.chenyunlong.qing.service.qmall.model.OrderMessage;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.util.backoff.FixedBackOff;

@RequiredArgsConstructor
public class KafkaConfig {

    private final KafkaTemplate<String, OrderMessage> kafkaTemplate;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();

        // 创建 DeadLetterPublishingRecoverer，指定将失败消息发送到哪个 DLQ 主题
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
                (record, exception) -> new TopicPartition(record.topic() + ".dlq", record.partition()));
        // 重试3次后，使用 recoverer 处理
        factory.setCommonErrorHandler(new SeekToCurrentErrorHandler(recoverer, new FixedBackOff(1000L, 3)));
        return factory;
    }
}
