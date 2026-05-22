package cn.chenyunlong.qing.auth.domain.user.command;

import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AssignRolesCommand {

    private UserId userId;
    private List<RoleId> roleIds;

    public static AssignRolesCommand create(Long userId, List<Long> roleIds) {
        return new AssignRolesCommand(
            UserId.of(userId),
            roleIds.stream().map(RoleId::of).toList()
        );
    }
}
