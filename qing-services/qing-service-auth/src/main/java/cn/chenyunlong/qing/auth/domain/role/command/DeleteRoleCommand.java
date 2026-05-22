package cn.chenyunlong.qing.auth.domain.role.command;

import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import lombok.Builder;
import lombok.Data;

/**
 * 删除角色命令
 */
@Data
@Builder
public class DeleteRoleCommand {

    private RoleId id;
}
