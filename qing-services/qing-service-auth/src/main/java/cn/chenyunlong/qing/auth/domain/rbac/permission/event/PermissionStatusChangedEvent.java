package cn.chenyunlong.qing.auth.domain.rbac.permission.event;

import cn.chenyunlong.qing.auth.domain.event.DomainEvent;
import cn.chenyunlong.qing.auth.domain.rbac.Operator;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionStatusChangedEvent extends DomainEvent {
    private final PermissionId id;
    private final PermissionStatus oldStatus;
    private final PermissionStatus newStatus;
    private final String reason;
    private final Operator operator;
    private final Instant now;

    public PermissionStatusChangedEvent(PermissionId id, PermissionStatus oldStatus, PermissionStatus newStatus, String reason, Operator operator, Instant now) {
        this.id = id;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.reason = reason;
        this.operator = operator;
        this.now = now;
    }
}
