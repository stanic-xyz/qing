package cn.chenyunlong.qing.auth.infrastructure.repository;

import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.RolePermission;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.RolePermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.rolepermission.permission.RolePermissionRepository;
import cn.chenyunlong.qing.auth.infrastructure.converter.RolePermissionMapper;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.RolePermissionEntity;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.RolePermissionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RolePermissionRepositoryImpl implements RolePermissionRepository {

    private final RolePermissionJpaRepository rolePermissionJpaRepository;
    private final RolePermissionMapper rolePermissionMapper;

    @Override
    public RolePermission save(RolePermission domain) {
        RolePermissionEntity rolePermissionEntity = rolePermissionMapper.toEntity(domain);
        rolePermissionJpaRepository.save(rolePermissionEntity);
        return domain;
    }

    @Override
    public Optional<RolePermission> findById(RolePermissionId rolePermissionId) {
        return rolePermissionJpaRepository.findById(rolePermissionId.id()).map(rolePermissionMapper::toDomain);
    }

    @Override
    public boolean existsByRoleIdAndPermissionId(RoleId roleId, PermissionId permissionId) {
        return rolePermissionJpaRepository.existsByRoleIdAndPermissionId(roleId.id(), permissionId.id());
    }

    @Override
    public void deleteByRoleIdAndPermissionId(RoleId roleId, PermissionId permissionId) {
        rolePermissionJpaRepository.deleteByRoleIdAndPermissionId(roleId.id(), permissionId.id());
    }
}
