package cn.chenyunlong.qing.auth.domain.rbac.rolepermission.permission;

import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.RolePermission;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.RolePermissionId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

import java.util.List;
import java.util.Set;

public interface RolePermissionRepository extends BaseRepository<RolePermission, RolePermissionId> {

    boolean existsByRoleIdAndPermissionId(RoleId roleId, PermissionId permissionId);

    void deleteByRoleIdAndPermissionId(RoleId roleId, PermissionId permissionId);

    Set<PermissionId> findPermissionIdsByRoleId(RoleId id);

    Set<PermissionId> findPermissionIdsByRoleIds(List<RoleId> roleIds);
}
