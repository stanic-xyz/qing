package cn.chenyunlong.qing.auth.domain.user.event;

import cn.chenyunlong.qing.auth.domain.event.DomainEvent;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import lombok.Getter;

import java.util.List;

@Getter
public class UserRolesChangedEvent extends DomainEvent {
    
    private final UserId userId;
    private final List<Long> roleIds;
    private final String reason;

    public UserRolesChangedEvent(Object source, UserId userId, List<Long> roleIds, String reason) {
        super(source);
        this.userId = userId;
        this.roleIds = roleIds;
        this.reason = reason;
    }
}
