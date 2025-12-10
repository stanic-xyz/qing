package cn.chenyunlong.qing.auth.domain.rbac.permission.exception;

import cn.chenyunlong.qing.auth.domain.authentication.exception.AuthenticationException;

/**
 * 权限重复异常
 */
public class PermissionDuplicateException extends AuthenticationException {
    public PermissionDuplicateException(String message) {
        super(message);
    }
}
