package cn.chenyunlong.qing.auth.application.event;

import cn.chenyunlong.qing.auth.domain.event.DomainEvent;
import cn.chenyunlong.qing.auth.domain.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class SpringDomainEventPublisher implements DomainEventPublisher {
    private final ApplicationEventPublisher delegate;

    @Override
    public void publish(DomainEvent event) {

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                // 发布消息
                delegate.publishEvent(event);
            }
        });
    }

    @Override
    public void publishAll(Collection<DomainEvent> events) {

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                // 发布消息
                events.forEach(delegate::publishEvent);
            }
        });

        events.forEach(delegate::publishEvent);
    }
}
