package cn.chenyunlong.qing.auth.domain.rbac.permission.exception;

import cn.chenyunlong.qing.auth.domain.authentication.exception.AuthenticationException;

/**
 * 权限不存在异常
 */
public class PermissionNotFoundException extends AuthenticationException {
    public PermissionNotFoundException(String message) {
        super(message);
    }
}
