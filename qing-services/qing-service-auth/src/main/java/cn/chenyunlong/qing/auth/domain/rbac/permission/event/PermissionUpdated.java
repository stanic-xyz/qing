package cn.chenyunlong.qing.auth.domain.rbac.permission.event;

import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;

/**
 * 权限更新事件
 */
public record PermissionUpdated(PermissionId permissionId) {
}

