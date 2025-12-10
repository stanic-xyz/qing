package cn.chenyunlong.qing.auth.infrastructure.event;

import cn.chenyunlong.qing.auth.domain.event.DomainEvent;
import cn.chenyunlong.qing.auth.domain.event.DomainEventPublisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class RabbitMQEventPublisher implements DomainEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        // 设置事件发布器
        //        DomainEventPublisher.setPublisher(this);
    }

    @Override
    public void publish(DomainEvent event) {
        rabbitTemplate.convertAndSend("event.exchange", "event.routing-key", event);
    }

    @Override
    public void publishAll(Collection<DomainEvent> events) {

    }
}
