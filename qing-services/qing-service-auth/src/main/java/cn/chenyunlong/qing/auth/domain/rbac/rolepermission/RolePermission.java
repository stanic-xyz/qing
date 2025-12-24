package cn.chenyunlong.qing.auth.domain.rbac.rolepermission;

import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.domain.common.BaseSimpleBusinessEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class RolePermission extends BaseSimpleBusinessEntity<RolePermissionId> {

    /**
     * 用户角色关联标识
     */
    private RolePermissionId id;

    /**
     * 角色ID
     */
    private RoleId roleId;

    /**
     * 权限ID
     */
    private PermissionId permissionId;

    public static RolePermission create(RoleId roleId, PermissionId permissionId) {
        return new RolePermission(RolePermissionId.generate(), roleId, permissionId);
    }
}
