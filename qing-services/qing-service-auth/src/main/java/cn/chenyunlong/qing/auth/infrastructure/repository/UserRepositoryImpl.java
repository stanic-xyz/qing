package cn.chenyunlong.qing.auth.infrastructure.repository;

import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.rbac.permission.Permission;
import cn.chenyunlong.qing.auth.domain.rbac.permission.repository.PermissionRepository;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.UserRole;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.UserRoleId;
import cn.chenyunlong.qing.auth.domain.role.repository.RoleRepository;
import cn.chenyunlong.qing.auth.domain.user.User;
import cn.chenyunlong.qing.auth.domain.user.repository.UserRepository;
import cn.chenyunlong.qing.auth.domain.user.valueObject.Email;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import cn.chenyunlong.qing.auth.domain.user.valueObject.Username;
import cn.chenyunlong.qing.auth.infrastructure.converter.UserMapper;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.QingUserEntity;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.RolePermissionEntity;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.UserRoleEntity;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.RolePermissionJpaRepository;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.UserConnectionJpaRepository;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.UserJpaRepository;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.UserRoleJpaRepository;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserConnectionJpaRepository userConnectionJpaRepository;
    private final UserMapper userMapper;
    private final UserRoleJpaRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionJpaRepository rolePermissionJpaRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public Optional<User> findByUsername(Username username) {
        QingUserEntity byUsername = userJpaRepository.findByUsername(username.value());
        return Optional.ofNullable(byUsername).map(this::getFullUser);
    }

    private User getFullUser(QingUserEntity userEntity) {
        User user = userMapper.entityToDomain(userEntity);

        List<UserRoleEntity> userRoleEntities = userRoleRepository.findByUserIdAndRevokedFalse(userEntity.getId());
        List<UserRole> userRoles = new ArrayList<>();

        for (UserRoleEntity userRoleEntity : userRoleEntities) {
            UserRole userRole = new UserRole();
            userRole.setId(new UserRoleId(userRoleEntity.getId()));
            userRole.setUserId(UserId.of(userRoleEntity.getUserId()));
            userRole.setRoleId(RoleId.of(userRoleEntity.getRoleId()));
            userRole.setRevoked(Boolean.TRUE.equals(userRoleEntity.getRevoked()));
            roleRepository.findById(RoleId.of(userRoleEntity.getRoleId())).ifPresent(userRole::setRole);

            List<PermissionId> permissionIds = rolePermissionJpaRepository.findByRoleId(userRoleEntity.getRoleId())
                    .stream()
                    .map(RolePermissionEntity::getPermissionId)
                    .map(PermissionId::of)
                    .distinct()
                    .toList();

            List<Permission> permissions = permissionRepository.findByIds(permissionIds);
            userRole.setPermissions(new HashSet<>(permissions));

            userRoles.add(userRole);
        }

        user.setRoles(userRoles);
        return user;
    }

    @Override
    public Optional<User> findUserByUserId(Long uid) {
        QingUserEntity userEntity = userJpaRepository.findUserByUserId(uid);
        return Optional.ofNullable(userEntity).map(userMapper::entityToDomain);
    }

    @Override
    public List<User> findByUserNames(Set<Username> nickNames) {
        if (CollUtil.isEmpty(nickNames)) {
            return CollUtil.newArrayList();
        }
        Set<String> usernames = nickNames.stream().map(Username::value).collect(Collectors.toSet());
        return userJpaRepository.findByNickNames(usernames).stream().map(userMapper::entityToDomain).toList();
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        if (email == null) {
            return Optional.empty();
        }
        QingUserEntity byEmail = userJpaRepository.findByEmail(email.value());
        return Optional.ofNullable(byEmail).map(userMapper::entityToDomain);
    }

    @Override
    public boolean existsByUsername(Username username) {
        return userJpaRepository.existsByUsername(username.value());
    }

    @Override
    public boolean existsByEmail(Email email) {
        return userJpaRepository.existsByEmail(email.value());
    }

    @Override
    public User save(User entity) {
        QingUserEntity userEntity = userMapper.toDataObject(entity);
        userJpaRepository.save(userEntity);
        return entity;
    }

    @Override
    public Optional<User> findById(UserId id) {
        return userJpaRepository.findById(id.id()).map(userMapper::entityToDomain);
    }

    @Override
    public boolean existsByNicknames(String nickname) {
        return userJpaRepository.existsByNickname(nickname);
    }
}
