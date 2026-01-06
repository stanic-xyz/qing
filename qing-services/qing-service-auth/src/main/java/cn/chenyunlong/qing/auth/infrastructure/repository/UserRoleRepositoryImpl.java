package cn.chenyunlong.qing.auth.infrastructure.repository;

import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.UserRole;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.UserRoleId;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.repository.UserRoleRepository;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import cn.chenyunlong.qing.auth.infrastructure.converter.UserRoleMapper;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.UserRoleEntity;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.UserRoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRoleRepositoryImpl implements UserRoleRepository {

    private final UserRoleJpaRepository userRoleJpaRepository;
    private final UserRoleMapper userRoleMapper;

    @Override
    public UserRole save(UserRole domain) {
        UserRoleEntity entity = userRoleMapper.toEntity(domain);
        userRoleJpaRepository.save(entity);
        return domain;
    }

    @Override
    public Optional<UserRole> findById(UserRoleId userRoleId) {
        Optional<UserRoleEntity> byId = userRoleJpaRepository.findById(userRoleId.id());
        return byId.map(userRoleMapper::toDomain);
    }

    @Override
    public boolean existsByUserIdAndRoleId(UserId id, RoleId roleId) {
        return userRoleJpaRepository.existsByUserIdAndRoleIdAndRevokedFalse(id.id(), roleId.id());
    }

    @Override
    public Optional<UserRole> findByUserIdAndRoleId(UserId userId, RoleId roleId) {
        return userRoleJpaRepository.findByUserIdAndRoleIdAndRevokedFalse(userId.id(), roleId.id()).map(userRoleMapper::toDomain);
    }

    @Override
    public List<UserRole> findByUserId(UserId userId) {
        return userRoleJpaRepository.findByUserIdAndRevokedFalse(userId.id())
                .stream()
                .map(userRoleMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean userHasRole(UserId userId, String roleCode) {
        // 这里需要实现检查用户是否拥有指定角色的逻辑
        // 由于涉及角色编码查询，可能需要扩展JPA接口或使用自定义查询
        List<UserRole> userRoles = findByUserId(userId);
        return userRoles.stream()
                .anyMatch(userRole -> userRole.getRole() != null &&
                        roleCode.equals(userRole.getRole().getCode()));
    }

    @Override
    public boolean userHasPermission(UserId userId, String permissionCode) {
        // 这里需要实现检查用户是否拥有指定权限的逻辑
        // 由于涉及权限编码查询，可能需要扩展JPA接口或使用自定义查询
        List<UserRole> userRoles = findByUserId(userId);
        return userRoles.stream()
                .anyMatch(userRole -> userRole.getRole() != null &&
                        userRole.getRole().getPermissionIds() != null &&
                        userRole.getRole().getPermissionIds().stream()
                                .anyMatch(permissionId -> {
                                    // TODO: 需要从权限仓库获取实际的Permission对象并检查权限编码
                                    // 这里暂时检查权限ID的字符串形式是否匹配权限编码
                                    return permissionCode.equals(permissionId.id().toString());
                                }));
    }
}
