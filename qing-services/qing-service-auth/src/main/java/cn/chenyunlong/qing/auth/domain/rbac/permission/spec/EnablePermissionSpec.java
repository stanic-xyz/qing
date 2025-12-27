package cn.chenyunlong.qing.auth.domain.rbac.permission.spec;

import cn.chenyunlong.qing.auth.domain.rbac.permission.Permission;

public class EnablePermissionSpec {

    /**
     * 验证权限是否可以启用
     */
    public boolean isSatisfiedBy(Permission permission) {
        // 1. 权限不能是系统内置的（如果是，不能禁用，但启用可以）
        // 2. 父权限必须已启用（如果有父权限）
        // 3. 权限当前状态必须是禁用状态

        if (permission.getParent() != null) {
            if (permission.getParent().isEnabled()) {
                return false; // 父权限未启用
            }
        }

        return !permission.isEnabled(); // 当前状态是禁用
    }
}
