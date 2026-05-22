package cn.chenyunlong.qing.auth.domain.role.command;

import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import lombok.Builder;
import lombok.Data;

/**
 * 更新角色命令
 */
@Data
@Builder
public class UpdateRoleCommand {

    private RoleId id;

    private String name;

    private String description;
}
