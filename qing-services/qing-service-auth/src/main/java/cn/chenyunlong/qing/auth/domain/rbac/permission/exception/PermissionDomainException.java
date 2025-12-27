package cn.chenyunlong.qing.auth.domain.rbac.permission.exception;

import cn.chenyunlong.qing.auth.domain.authentication.exception.AuthenticationException;

public class PermissionDomainException extends AuthenticationException {
    public PermissionDomainException(String message) {
        super(message);
    }
}
