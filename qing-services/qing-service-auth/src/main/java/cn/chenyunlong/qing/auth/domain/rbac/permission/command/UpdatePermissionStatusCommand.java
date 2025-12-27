package cn.chenyunlong.qing.auth.domain.rbac.permission.command;

import cn.chenyunlong.qing.auth.domain.rbac.Operator;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 删除权限命令
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePermissionStatusCommand {

    private PermissionId id;

    private PermissionStatus status;

    private String reason;

    private Operator operator;
}
