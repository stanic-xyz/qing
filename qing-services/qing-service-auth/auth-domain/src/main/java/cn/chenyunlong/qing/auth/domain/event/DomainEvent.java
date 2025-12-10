package cn.chenyunlong.qing.auth.domain.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class DomainEvent {
    private final String timestamp = LocalDateTime.now().toString();
}
