package cn.chenyunlong.qing.anime.interfaces.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagePriorityConfiguration {

    // 核心消息队列 - 高优先级
    @Bean
    public Queue coreProcessQueue() {
        return QueueBuilder.durable("core.process.queue")
                .deadLetterExchange("dlx.exchange")
                .deadLetterRoutingKey("core.process.dlq")
                .maxPriority(10) // 支持优先级
                .build();
    }

    // 通知消息队列 - 中优先级
    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable("notification.queue")
                .deadLetterExchange("dlx.exchange")
                .deadLetterRoutingKey("notification.dlq")
                .maxPriority(5)
                .ttl(600000) // 10分钟TTL
                .build();
    }

    // 监控消息队列 - 低优先级，可丢弃
    @Bean
    public Queue monitoringQueue() {
        return QueueBuilder.durable("monitoring.queue")
                .deadLetterExchange("dlx.exchange")
                .deadLetterRoutingKey("monitoring.dlq")
                .overflow(QueueBuilder.Overflow.dropHead) // 限制队列长度
                .maxLengthBytes(100000000) // 限制队列大小
                .build();
    }
}
