package cn.chenyunlong.qing.auth.infrastructure.repository.memory;

import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.UserRole;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.UserRoleId;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.repository.UserRoleRepository;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class InMemoryUserRoleRepository implements UserRoleRepository {
    @Override
    public boolean existsByUserIdAndRoleId(UserId id, RoleId roleNotFoundId) {
        return false;
    }

    @Override
    public Optional<UserRole> findByUserIdAndRoleId(UserId userId, RoleId roleId) {
        return Optional.empty();
    }

    @Override
    public UserRole save(UserRole entity) {
        return null;
    }

    @Override
    public Optional<UserRole> findById(UserRoleId userRoleId) {
        return Optional.empty();
    }
}
