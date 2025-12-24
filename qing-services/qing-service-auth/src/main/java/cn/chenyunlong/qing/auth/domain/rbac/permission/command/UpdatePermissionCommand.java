package cn.chenyunlong.qing.auth.domain.rbac.permission.command;

import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 更新权限命令
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePermissionCommand {

    private PermissionId id;
    private String name;
    private String description;
    private String resource;
    private String action;
    private Integer sortOrder;
}
