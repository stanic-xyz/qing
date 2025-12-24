package cn.chenyunlong.qing.auth.domain.rbac.rolepermission.command;

import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;

public record RemovePermissionFromRoleCommand(RoleId roleId, PermissionId permissionId) {
    public static RemovePermissionFromRoleCommand create(Long roleId, Long permissionId) {
        return new RemovePermissionFromRoleCommand(RoleId.of(roleId), PermissionId.of(permissionId));
    }
}
