package cn.chenyunlong.qing.auth.domain.event;

import java.util.Collection;

/**
 * 领域事件发布器端口
 */
public interface DomainEventPublisher {
    /**
     * 发布单个事件
     */
    void publish(DomainEvent event);

    /**
     * 批量发布事件
     */
    void publishAll(Collection<DomainEvent> events);
}
