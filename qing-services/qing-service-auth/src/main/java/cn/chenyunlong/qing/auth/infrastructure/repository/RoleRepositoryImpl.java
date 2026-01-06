package cn.chenyunlong.qing.auth.infrastructure.repository;

import cn.chenyunlong.qing.auth.domain.rbac.Role;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.role.repository.RoleRepository;
import cn.chenyunlong.qing.auth.infrastructure.converter.RoleMapper;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.RoleEntity;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.RolePermissionEntity;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.RoleJpaRepository;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.RolePermissionJpaRepository;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;
    private final RolePermissionJpaRepository rolePermissionJpaRepository;
    private final RoleMapper roleMapper;


    @Override
    public List<Role> findByIds(List<RoleId> roleIds) {
        // 批量根据标识查询角色并映射为领域对象
        if (CollUtil.isEmpty(roleIds)) {
            return java.util.Collections.emptyList();
        }
        List<Long> ids = roleIds.stream().map(RoleId::id).toList();
        return roleJpaRepository.findAllById(ids)
                .stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Role save(Role domain) {
        RoleEntity roleEntity = roleMapper.domain2Entity(domain);
        roleJpaRepository.save(roleEntity);

        // 维护权限关联
        updateRolePermissions(domain);
        return domain;
    }

    @Override
    public Optional<Role> findById(RoleId id) {
        return roleJpaRepository.findById(id.id()).map(this::mapToDomain);
    }

    @Override
    public boolean existsByCode(String code) {
        return roleJpaRepository.existsByCode(code);
    }

    @Override
    public boolean existsByName(String name) {
        return roleJpaRepository.existsByName(name);
    }

    private Role mapToDomain(RoleEntity entity) {
        Role role = roleMapper.entity2Domain(entity);
        if (role != null) {
            // 加载权限ID
            List<RolePermissionEntity> rolePermissions = rolePermissionJpaRepository.findByRoleId(entity.getId());
            Set<PermissionId> permissionIds = rolePermissions.stream()
                    .map(rp -> PermissionId.of(rp.getPermissionId()))
                    .collect(Collectors.toSet());
            role.setPermissionIds(permissionIds);
        }
        return role;
    }

    private void updateRolePermissions(Role role) {
        Long roleId = role.getId().id();
        Set<Long> newPermissionIds = role.getPermissionIds().stream()
                .map(PermissionId::id)
                .collect(Collectors.toSet());

        List<RolePermissionEntity> existingAssociations = rolePermissionJpaRepository.findByRoleId(roleId);
        Set<Long> existingPermissionIds = existingAssociations.stream()
                .map(RolePermissionEntity::getPermissionId)
                .collect(Collectors.toSet());

        // 需要添加的
        List<RolePermissionEntity> toAdd = newPermissionIds.stream()
                .filter(id -> !existingPermissionIds.contains(id))
                .map(permissionId -> {
                    RolePermissionEntity entity = new RolePermissionEntity();
                    entity.setId(IdUtil.getSnowflakeNextId());
                    entity.setRoleId(roleId);
                    entity.setPermissionId(permissionId);
                    return entity;
                })
                .toList();

        // 需要删除的
        List<RolePermissionEntity> toRemove = existingAssociations.stream()
                .filter(entity -> !newPermissionIds.contains(entity.getPermissionId()))
                .toList();

        if (CollUtil.isNotEmpty(toAdd)) {
            rolePermissionJpaRepository.saveAll(toAdd);
        }
        if (CollUtil.isNotEmpty(toRemove)) {
            rolePermissionJpaRepository.deleteAll(toRemove);
        }
    }
}
