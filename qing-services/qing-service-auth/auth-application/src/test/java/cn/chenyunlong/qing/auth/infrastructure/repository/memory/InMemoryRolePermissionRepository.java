package cn.chenyunlong.qing.auth.infrastructure.repository.memory;

import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.RolePermission;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.RolePermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.permission.RolePermissionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class InMemoryRolePermissionRepository implements RolePermissionRepository {
    @Override
    public boolean existsByRoleIdAndPermissionId(RoleId roleId, PermissionId permissionId) {
        return false;
    }

    @Override
    public void deleteByRoleIdAndPermissionId(RoleId roleId, PermissionId permissionId) {

    }

    @Override
    public RolePermission save(RolePermission entity) {
        return null;
    }

    @Override
    public Optional<RolePermission> findById(RolePermissionId rolePermissionId) {
        return Optional.empty();
    }
}
