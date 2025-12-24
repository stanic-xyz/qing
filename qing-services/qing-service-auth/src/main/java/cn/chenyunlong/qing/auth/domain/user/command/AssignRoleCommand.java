package cn.chenyunlong.qing.auth.domain.user.command;

import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class AssignRoleCommand {

    private UserId userId;
    private RoleId roleId;

    public static AssignRoleCommand create(Long userId, Long roleId) {
        return new AssignRoleCommand(UserId.of(userId), RoleId.of(roleId));
    }
}
