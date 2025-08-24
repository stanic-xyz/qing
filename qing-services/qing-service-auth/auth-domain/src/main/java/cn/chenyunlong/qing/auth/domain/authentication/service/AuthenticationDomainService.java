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
 *
 */

package cn.chenyunlong.qing.auth.domain.authentication.service;

import cn.chenyunlong.qing.auth.domain.authentication.Authentication;
import cn.chenyunlong.qing.auth.domain.authentication.AuthenticationId;
import cn.chenyunlong.qing.auth.domain.authentication.AuthenticationType;
import cn.chenyunlong.qing.auth.domain.authentication.exception.AuthenticationException;
import cn.chenyunlong.qing.auth.domain.authentication.repository.AuthenticationRepository;
import cn.chenyunlong.qing.auth.domain.user.QingUser;
import cn.chenyunlong.qing.auth.domain.user.QingUserId;
import cn.chenyunlong.qing.auth.domain.user.repository.UserRepository;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.BCrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 认证领域服务
 * 负责处理认证相关的领域逻辑
 *
 * @author 陈云龙
 */
@Service
@RequiredArgsConstructor
public class AuthenticationDomainService {

    private final AuthenticationRepository authenticationRepository;
    private final UserRepository userRepository;

    /**
     * 用户名密码认证
     *
     * @param username  用户名
     * @param password  密码
     * @param ipAddress IP地址
     * @param userAgent 用户代理
     * @return 认证结果
     */
    public Authentication authenticateByUsernamePassword(String username, String password, String ipAddress, String userAgent) {
        // 创建认证记录
        Authentication authentication = Authentication.create(
                AuthenticationId.of(IdUtil.getSnowflakeNextId()),
                AuthenticationType.USERNAME_PASSWORD,
                username,
                "[PROTECTED]", // 不保存明文密码
                ipAddress,
                userAgent
        );

        try {
            // 查找用户
            QingUser user = userRepository.findByUsername(username);
            if (user == null) {
                authentication.fail("用户不存在");
                return authenticationRepository.save(authentication);
            }

            // 检查用户状态
            if (!user.isActive()) {
                authentication.fail("用户未激活");
                return authenticationRepository.save(authentication);
            }

            if (user.isLocked()) {
                authentication.fail("用户已锁定");
                return authenticationRepository.save(authentication);
            }

            // 验证密码
            if (!BCrypt.checkpw(password, user.getEncodedPassword())) {
                authentication.fail("密码错误");
                return authenticationRepository.save(authentication);
            }

            // 认证成功
            authentication.succeed(user);
            return authenticationRepository.save(authentication);

        } catch (Exception e) {
            authentication.fail("认证过程发生错误: " + e.getMessage());
            return authenticationRepository.save(authentication);
        }
    }

    /**
     * JWT令牌认证
     *
     * @param userId    用户ID
     * @param ipAddress IP地址
     * @param userAgent 用户代理
     * @return 认证结果
     */
    public Authentication authenticateByJwtToken(Long userId, String ipAddress, String userAgent) {
        // 创建认证记录
        Authentication authentication = Authentication.create(
                AuthenticationId.of(IdUtil.getSnowflakeNextId()),
                AuthenticationType.JWT_TOKEN,
                userId.toString(),
                "[JWT_TOKEN]", // 不保存令牌
                ipAddress,
                userAgent
        );

        try {
            // 查找用户
            QingUser user = userRepository.findById(QingUserId.of(userId))
                    .orElseThrow(() -> new AuthenticationException("用户不存在"));

            // 检查用户状态
            if (!user.isActive()) {
                authentication.fail("用户未激活");
                return authenticationRepository.save(authentication);
            }

            if (user.isLocked()) {
                authentication.fail("用户已锁定");
                return authenticationRepository.save(authentication);
            }

            // 认证成功
            authentication.succeed(user);
            return authenticationRepository.save(authentication);

        } catch (Exception e) {
            authentication.fail("认证过程发生错误: " + e.getMessage());
            return authenticationRepository.save(authentication);
        }
    }
}
