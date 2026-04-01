package cn.chenyunlong.qing.service.llm.event;

import org.springframework.context.ApplicationEvent;

public class RelChangeEvent extends ApplicationEvent {
    public RelChangeEvent(Object source) {
        super(source);
    }
}
