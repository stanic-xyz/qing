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

package cn.chenyunlong.qing.domain.user.service;

import cn.chenyunlong.qing.domain.permission.Permission;
import cn.chenyunlong.qing.domain.user.User;
import cn.chenyunlong.qing.infrastructure.exception.AbstractException;
import cn.chenyunlong.qing.infrastructure.model.params.LoginParam;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author stan
 * @date 2018/6/3
 */
@Component
public interface UserService {

    /**
     * 通过用户查询用户
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    User findUserByUserId(Long userId);


    /**
     * 查询用户信息
     *
     * @return 所有用户信息
     */
    List<User> findAll();

    /**
     * 更新用户的密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param userId      用户ID
     * @return 放弃上面的东西
     */
    User updatePassword(String oldPassword, String newPassword, Long userId);

    /**
     * 注册
     *
     * @param userInfo 用户信息
     * @return 返回用户信息
     */
    User register(User userInfo);

    /**
     * 登陆
     *
     * @param user 用户登录信息
     * @return 用户信息
     */
    User login(User user);

    /**
     * 根据名称获取用户舒适信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User findUserByUsername(String username);

    /**
     * 获取用户权限信息
     *
     * @param username 用户名
     * @return 用户权限信息
     */
    List<Permission> getPermissionByUsername(String username);

    /**
     * 添加用户信息
     *
     * @param user 用户信息
     * @return 添加后的用户信息
     * @throws AbstractException 异常
     */
    User addUserInfo(User user) throws AbstractException;

    /**
     * 根据Email获取用户信息
     *
     * @param email email
     * @return 用户信息
     */
    User findUserByEmail(String email);

    /**
     * 检查密码是否匹配用户的密码
     *
     * @param userInfo 用户信息
     * @param password 输入的密码
     * @return 密码匹配结果
     */
    boolean passwordMatch(@NotNull User userInfo, String password);

    /**
     * 认证
     *
     * @param loginParam 登录参数
     * @return 取消操作了
     */
    User authenticate(LoginParam loginParam);

    /**
     * 根据第三方ID进行登录
     *
     * @param authUser 第三方唯一ID
     * @return 本地用户
     */
    User getUserInfoByThird(AuthUser authUser);
}
