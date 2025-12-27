package cn.chenyunlong.qing.auth.domain.role.exception;

import cn.chenyunlong.qing.auth.domain.rbac.RoleId;
import cn.chenyunlong.qing.domain.common.exception.DomainException;

// 具体领域异常
public class RoleNotFoundException extends DomainException {
    public RoleNotFoundException(RoleId roleId) {
        super("角色不存在: " + roleId);
    }
}
