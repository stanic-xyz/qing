package cn.chenyunlong.qing.auth.domain.rbac.userrole.repository;

import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.UserRole;
import cn.chenyunlong.qing.auth.domain.rbac.userrole.UserRoleId;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

import java.util.Optional;

public interface UserRoleRepository extends BaseRepository<UserRole, UserRoleId> {

    boolean existsByUserIdAndRoleId(UserId id, RoleId roleNotFoundId);

    Optional<UserRole> findByUserIdAndRoleId(UserId userId, RoleId roleId);
}
