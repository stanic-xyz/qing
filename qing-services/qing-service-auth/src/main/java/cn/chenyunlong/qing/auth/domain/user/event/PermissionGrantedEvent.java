package cn.chenyunlong.qing.auth.domain.user.event;

import cn.chenyunlong.qing.auth.domain.event.DomainEvent;
import cn.chenyunlong.qing.auth.domain.user.User;
import cn.chenyunlong.qing.auth.domain.user.valueObject.Permission;
import lombok.Getter;

@Getter
public class PermissionGrantedEvent extends DomainEvent {
    private final User user;
    private final Permission permission;

    public PermissionGrantedEvent(Object source, User user, Permission permission) {
        super(source);
        this.user = user;
        this.permission = permission;
    }
}
