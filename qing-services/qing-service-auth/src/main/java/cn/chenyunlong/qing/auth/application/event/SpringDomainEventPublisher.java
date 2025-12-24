package cn.chenyunlong.qing.auth.application.event;

import cn.chenyunlong.qing.auth.domain.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(Object event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishAll(Collection<Object> events) {
        events.forEach(applicationEventPublisher::publishEvent);
    }
}
