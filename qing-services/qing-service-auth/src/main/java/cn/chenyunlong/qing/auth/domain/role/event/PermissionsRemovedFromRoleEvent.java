package cn.chenyunlong.qing.auth.domain.role.event;

import cn.chenyunlong.qing.auth.domain.event.DomainEvent;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionsRemovedFromRoleEvent extends DomainEvent {
    private RoleId roleId;
    private Collection<PermissionId> permissionIds;

    public PermissionsRemovedFromRoleEvent(RoleId roleId, Collection<PermissionId> permissionIds) {
        this.roleId = roleId;
        this.permissionIds = permissionIds;
    }
}
