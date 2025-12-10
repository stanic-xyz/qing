package cn.chenyunlong.qing.auth.domain.rbac.permission.event;

import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import lombok.AllArgsConstructor;

/**
 * 权限创建事件
 */
@AllArgsConstructor
public record PermissionCreated(PermissionId permissionId, String code, String name) {
}
