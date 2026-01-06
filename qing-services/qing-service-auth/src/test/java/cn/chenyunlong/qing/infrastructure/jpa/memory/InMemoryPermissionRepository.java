package cn.chenyunlong.qing.infrastructure.jpa.memory;

import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.permission.Permission;
import cn.chenyunlong.qing.auth.domain.rbac.permission.repository.PermissionRepository;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryPermissionRepository implements PermissionRepository {

    private final Map<PermissionId, Permission> store = new ConcurrentHashMap<>();

    @Override
    public boolean existsByCode(String code) {
        return store.values().stream().anyMatch(p -> StrUtil.equals(p.getCode(), code));
    }

    @Override
    public boolean existsByName(String name) {
        return store.values().stream().anyMatch(p -> StrUtil.equals(p.getName(), name));
    }

    @Override
    public List<Permission> findByIds(List<PermissionId> permissionIds) {
        return store.values().stream().filter(p -> CollUtil.contains(permissionIds, p.getId())).toList();
    }

    @Override
    public Permission save(Permission entity) {
        return store.put(entity.getId(), entity);
    }

    @Override
    public Optional<Permission> findById(PermissionId permissionId) {
        return store.values().stream().filter(p -> Objects.equals(permissionId.id(), p.getId().id())).findFirst();
    }
}
