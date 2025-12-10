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

import java.util.Optional;

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
}
