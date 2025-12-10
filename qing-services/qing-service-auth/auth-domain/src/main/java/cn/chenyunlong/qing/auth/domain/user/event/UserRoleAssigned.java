package cn.chenyunlong.qing.auth.domain.user.event;

import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;

public class UserRoleAssigned {
    public UserRoleAssigned(UserId userId, RoleId userRole) {}
}
