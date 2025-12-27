package cn.chenyunlong.qing.auth.domain.rbac.permission.exception;

import cn.chenyunlong.qing.auth.domain.authentication.exception.AuthenticationException;
import cn.chenyunlong.qing.auth.domain.rbac.PermissionId;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限不存在异常
 */
@Getter
public class PermissionNotFoundException extends AuthenticationException {

    private final List<PermissionId> permissionIds;

    public PermissionNotFoundException(String message) {
        super(message);
        permissionIds = new ArrayList<>();
    }

    public PermissionNotFoundException(List<PermissionId> permissionIds) {
        super(StrUtil.format("权限【{}】不存在", permissionIds));
        this.permissionIds = permissionIds;
    }

}
