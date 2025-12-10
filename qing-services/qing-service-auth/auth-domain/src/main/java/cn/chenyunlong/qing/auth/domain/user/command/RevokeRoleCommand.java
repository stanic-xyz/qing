package cn.chenyunlong.qing.auth.domain.user.command;

import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RevokeRoleCommand {

    private UserId userId;
    private RoleId roleId;

    public static RevokeRoleCommand create(Long userId, Long roleId) {
        return new RevokeRoleCommand(UserId.of(userId), RoleId.of(roleId));
    }
}
