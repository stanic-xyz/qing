package cn.chenyunlong.qing.service.llm.event;

import org.springframework.context.ApplicationEvent;

public class AccountChangeEvent extends ApplicationEvent {
    public AccountChangeEvent(Object source) {
        super(source);
    }
}
