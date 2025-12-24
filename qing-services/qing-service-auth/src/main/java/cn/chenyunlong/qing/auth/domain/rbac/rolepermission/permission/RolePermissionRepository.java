package cn.chenyunlong.qing.auth.domain.rbac.rolepermission.permission;

import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.RolePermission;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.RolePermissionId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

public interface RolePermissionRepository extends BaseRepository<RolePermission, RolePermissionId> {

    boolean existsByRoleIdAndPermissionId(RoleId roleId, PermissionId permissionId);

    void deleteByRoleIdAndPermissionId(RoleId roleId, PermissionId permissionId);
}
