/*
 * Copyright (c) 2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.domain.permission.mapper;

import cn.chenyunlong.qing.domain.permission.Permission;
import cn.chenyunlong.qing.domain.permission.creator.PermissionCreator;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

class PermissionMapperTest {

    @Test
    void dtoToEntity() {

        PermissionCreator permissionCreator = new PermissionCreator();
        permissionCreator.setDescription("写入");
        permissionCreator.setName("write");

        Permission permission = PermissionMapper.INSTANCE.dtoToEntity(permissionCreator);

        Assert.isTrue("write".equals(permission.getName()), "名称错误了");
        Assert.isTrue("写入".equals(permission.getDescription()), "描述信息错误");
    }
}
