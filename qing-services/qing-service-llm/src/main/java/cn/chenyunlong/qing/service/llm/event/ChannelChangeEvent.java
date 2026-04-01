package cn.chenyunlong.qing.service.llm.event;

import org.springframework.context.ApplicationEvent;

public class ChannelChangeEvent extends ApplicationEvent {
    public ChannelChangeEvent(Object source) {
        super(source);
    }
}
