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

import cn.chenyunlong.qing.domain.common.Identifiable;

/**
 * 权限ID值对象
 *
 * @author 陈云龙
 * @since 1.0.0
 */
public record PermissionId(Long id) implements Identifiable<Long> {

    public static PermissionId generate() {
        return new PermissionId(Identifiable.simpleGenerate());
    }

    public static PermissionId of(Long permissionId) {
        return new PermissionId(permissionId);
    }
}
