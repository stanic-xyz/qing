package cn.chenyunlong.qing.auth.domain.user.command;

import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class AssignPermissionCommand {

    private PermissionId permissionId;
    private RoleId roleId;

    public static AssignPermissionCommand create(Long userId, Long roleId) {
        return new AssignPermissionCommand(PermissionId.of(userId), RoleId.of(roleId));
    }
}
