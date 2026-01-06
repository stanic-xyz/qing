package cn.chenyunlong.qing.infrastructure.jpa.memory;

import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.UserRole;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.UserRoleId;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.repository.UserRoleRepository;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class InMemoryUserRoleRepository implements UserRoleRepository {

    private final Set<UserRole> userRoles = new HashSet<>();

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
        boolean add = userRoles.add(entity);
        return entity;
    }

    @Override
    public Optional<UserRole> findById(UserRoleId userRoleId) {
        return Optional.empty();
    }

    @Override
    public List<UserRole> findByUserId(UserId userId) {
        return List.of();
    }

    @Override
    public boolean userHasRole(UserId userId, String roleCode) {
        return false;
    }

    @Override
    public boolean userHasPermission(UserId userId, String permissionCode) {
        return false;
    }
}
