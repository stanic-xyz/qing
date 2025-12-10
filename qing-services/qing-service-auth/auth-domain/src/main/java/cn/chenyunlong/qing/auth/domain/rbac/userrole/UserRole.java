package cn.chenyunlong.qing.auth.domain.rbac.userrole;

import cn.chenyunlong.qing.auth.domain.rbac.Role;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.rbac.permission.Permission;
import cn.chenyunlong.qing.auth.domain.user.event.UserRoleAssigned;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import cn.chenyunlong.qing.domain.common.AuditInfo;
import cn.chenyunlong.qing.domain.common.BaseSimpleBusinessEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
public class UserRole extends BaseSimpleBusinessEntity<UserRoleId> {

    private UserRoleId id;
    private UserId userId;
    private RoleId roleId;
    private Boolean revoked = false;
    private Integer version = 0;
    private Role role;
    private Set<Permission> permissions;

    private UserRole(UserId userId, RoleId roleId) {
        this.id = UserRoleId.nextId();
        this.userId = userId;
        this.roleId = roleId;
    }

    public static UserRole create(UserId userId, RoleId roleId) {
        UserRole userRole = new UserRole(userId, roleId);
        userRole.setAuditInfo(AuditInfo.createSystem());
        return userRole;
    }

    public void revoke() {
        if (this.revoked) {
            throw new IllegalStateException("关联已被取消");
        }
        // 标记未已移除
        revoked = true;
        registerEvent(new UserRoleAssigned(getUserId(), getRoleId()));
    }
}
