package cn.chenyunlong.qing.infrastructure.repository.memory;

import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.RolePermission;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.RolePermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.permission.RolePermissionRepository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryRolePermissionRepository implements RolePermissionRepository {

    private final Map<RolePermissionId, RolePermission> store = new ConcurrentHashMap<>();

    @Override
    public boolean existsByRoleIdAndPermissionId(RoleId roleId, PermissionId permissionId) {
        return false;
    }

    @Override
    public void deleteByRoleIdAndPermissionId(RoleId roleId, PermissionId permissionId) {

    }

    @Override
    public Set<PermissionId> findPermissionIdsByRoleId(RoleId id) {
        return store.values().stream().filter(rolePermission -> rolePermission.getRoleId().equals(id)).map(RolePermission::getPermissionId).collect(Collectors.toSet());
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
