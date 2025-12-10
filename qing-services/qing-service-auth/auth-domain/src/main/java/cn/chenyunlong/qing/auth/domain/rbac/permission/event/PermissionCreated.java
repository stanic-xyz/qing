package cn.chenyunlong.qing.auth.domain.rbac.permission.event;

import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;

/**
 * 权限创建事件
 */
public record PermissionCreated(PermissionId permissionId, String code, String name) {
}
