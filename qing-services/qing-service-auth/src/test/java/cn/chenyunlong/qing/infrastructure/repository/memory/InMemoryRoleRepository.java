package cn.chenyunlong.qing.infrastructure.repository.memory;

import cn.chenyunlong.qing.auth.domain.rbac.Role;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.role.repository.RoleRepository;
import cn.hutool.core.util.StrUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

/**
 * 内存 Role 仓库，仅用于测试
 */
@Repository
public class InMemoryRoleRepository implements RoleRepository {

    private final Map<RoleId, Role> store = new ConcurrentHashMap<>();

    @Override
    public Role save(Role entity) {
        store.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<Role> findById(RoleId id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Role> findByIds(List<RoleId> ids) {
        return ids.stream()
                .map(store::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByCode(String code) {
        return store.values().stream().anyMatch(role -> StrUtil.equals(role.getCode(), code));
    }

    @Override
    public boolean existsByName(String name) {
        return store.values().stream().anyMatch(role -> StrUtil.equals(role.getName(), name));
    }
}
