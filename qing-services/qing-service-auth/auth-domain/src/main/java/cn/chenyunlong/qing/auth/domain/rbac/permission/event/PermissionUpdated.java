package cn.chenyunlong.qing.auth.domain.rbac.permission.event;

import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import lombok.AllArgsConstructor;

/**
 * 权限更新事件
 */
@AllArgsConstructor
public record PermissionUpdated(PermissionId permissionId) {
}

