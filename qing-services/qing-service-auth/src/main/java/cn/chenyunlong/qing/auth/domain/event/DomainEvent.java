package cn.chenyunlong.qing.auth.domain.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public abstract class DomainEvent extends ApplicationEvent {

    public DomainEvent(Object source) {
        super(source);
    }
}
