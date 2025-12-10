/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.chenyunlong.qing.auth.domain.rbac;

/**
 * 权限类型枚举
 *
 * @author 陈云龙
 * @since 1.0.0
 */
public enum PermissionType {

    /**
     * 菜单权限
     * 控制用户可以访问的菜单和页面
     */
    MENU("菜单权限"),

    /**
     * 操作权限
     * 控制用户可以执行的操作，如增删改查
     */
    OPERATION("操作权限"),

    /**
     * 数据权限
     * 控制用户可以访问的数据范围
     */
    DATA("数据权限");

    private final String description;

    PermissionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
