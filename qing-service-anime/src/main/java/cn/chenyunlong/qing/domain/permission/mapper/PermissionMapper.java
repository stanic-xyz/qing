/*
 * Copyright (c) 2019-2022 YunLong Chen
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
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Stan
 */
@Mapper
@Component
public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 获取用户的权限信息
     *
     * @param username 用户名称
     * @return 遍历
     */
    @Select("SELECT DISTINCT permission.name AS NAME\n" +
            "FROM USER,\n" +
            "     user_role,\n" +
            "     role_permission,\n" +
            "     permission,\n" +
            "     role\n" +
            "WHERE user.uid = user_role.user_id\n" +
            "  AND user_role.role_id\n" +
            "  AND role.role_id\n" +
            "  AND role_permission.role_id = role.role_id\n" +
            "  AND role_permission.permission_id = permission.id\n" +
            "  AND user.username=#{username}")
    List<Permission> getPermissionByUsername(String username);

}
