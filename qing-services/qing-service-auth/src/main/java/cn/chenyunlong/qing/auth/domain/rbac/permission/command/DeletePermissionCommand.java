package cn.chenyunlong.qing.auth.domain.rbac.permission.command;

import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
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
public class DeletePermissionCommand {

    private PermissionId id;
}
