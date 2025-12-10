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
 * 角色类型枚举
 *
 * @author 陈云龙
 * @since 1.0.0
 */
public enum RoleType {

    /**
     * 系统角色
     * 系统预定义的角色，不能删除和修改
     */
    SYSTEM("系统角色"),

    /**
     * 自定义角色
     * 用户自定义的角色，可以删除和修改
     */
    CUSTOM("自定义角色");

    private final String description;

    RoleType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
