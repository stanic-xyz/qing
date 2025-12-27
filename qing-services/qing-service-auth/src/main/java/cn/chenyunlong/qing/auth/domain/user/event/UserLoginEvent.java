package cn.chenyunlong.qing.auth.domain.user.event;

import cn.chenyunlong.qing.auth.domain.event.DomainEvent;
import cn.chenyunlong.qing.auth.domain.user.User;
import lombok.Getter;

@Getter
public class UserLoginEvent extends DomainEvent {
    private final User user;
    private final String ip;
    private final String userAgent;

    public UserLoginEvent(Object source, User user, String ip, String userAgent) {
        super(source);
        this.user = user;
        this.ip = ip;
        this.userAgent = userAgent;
    }
}
