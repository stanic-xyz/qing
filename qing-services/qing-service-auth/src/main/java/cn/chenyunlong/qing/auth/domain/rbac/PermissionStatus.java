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

import lombok.Getter;

/**
 * 权限状态枚举
 *
 * @author 陈云龙
 * @since 1.0.0
 */
@Getter
public enum PermissionStatus {

    /**
     * 启用状态
     */
    ENABLED("启用"),

    /**
     * 禁用状态
     */
    DISABLED("禁用");

    private final String description;

    PermissionStatus(String description) {
        this.description = description;
    }

}
