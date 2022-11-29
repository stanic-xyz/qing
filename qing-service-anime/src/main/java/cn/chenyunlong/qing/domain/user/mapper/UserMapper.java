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

package cn.chenyunlong.qing.domain.user.mapper;

import cn.chenyunlong.qing.domain.user.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
 * 用户信息
 *
 * @author Stan
 */
@Mapper
@Component
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Select("select * from user where username = #{username}")
    User findByUsername(@Param("username") String username);

    /**
     * 添加用户信息
     *
     * @param user 待添加的用户信息
     */
    @Insert("INSERT INTO `user`(`username`,`password`,`phone`,`open_id`,`email`)VALUES(#{username},#{password}},#{phone},#{openId},#{email})")
    void addUser(User user);

    /**
     * 根据用户的email获取用户信息
     *
     * @param email 用户的email
     * @return 用户信息
     */
    @Select("select * from user where email = #{email}")
    User findByEmail(@Param("email") String email);

    /**
     * 更新用户的密码
     *
     * @param user 更新后的用户信息
     */
    @Update("update user set password=#{user.password} where userid=#{user.userId}")
    void updatePassword(@Param("user") User user);
}