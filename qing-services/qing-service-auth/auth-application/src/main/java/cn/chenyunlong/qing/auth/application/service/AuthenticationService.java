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

package cn.chenyunlong.qing.auth.application.service;

import cn.chenyunlong.qing.auth.application.dto.AuthenticationResult;
import cn.chenyunlong.qing.auth.application.dto.LoginRequest;
import cn.chenyunlong.qing.auth.application.dto.TokenInfo;
import cn.chenyunlong.qing.auth.application.utils.AuthJwtTokenUtil;
import cn.chenyunlong.qing.auth.domain.authentication.Authentication;
import cn.chenyunlong.qing.auth.domain.authentication.AuthenticationToken;
import cn.chenyunlong.qing.auth.domain.authentication.exception.AuthenticationException;
import cn.chenyunlong.qing.auth.domain.authentication.service.AuthenticationDomainService;
import cn.chenyunlong.qing.auth.domain.authentication.service.TokenDomainService;
import cn.chenyunlong.qing.auth.domain.user.QingUser;
import cn.chenyunlong.qing.auth.domain.user.QingUserId;
import cn.chenyunlong.qing.auth.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 认证应用服务
 * 负责处理认证相关的应用层逻辑
 *
 * @author 陈云龙
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationDomainService authenticationDomainService;
    private final TokenDomainService tokenDomainService;
    private final UserRepository userRepository;
    private final AuthJwtTokenUtil authJwtTokenUtil;

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @param ipAddress    IP地址
     * @param userAgent    用户代理
     * @return 认证结果
     */
    @Transactional
    public AuthenticationResult login(LoginRequest loginRequest, String ipAddress, String userAgent) {
        // 使用领域服务进行认证
        Authentication authentication = authenticationDomainService.authenticateByUsernamePassword(
            loginRequest.getUsername(),
            loginRequest.getPassword(),
            ipAddress,
            userAgent
        );

        // 检查认证结果
        if (!authentication.isSuccessful()) {
            return AuthenticationResult.failure(authentication.getFailureReason());
        }

        // 获取用户信息
        QingUserId userId = authentication.getUserId();
        QingUser user = userRepository.findById(userId)
            .orElseThrow(() -> new AuthenticationException("用户不存在"));

        // 生成JWT令牌
        String jwtToken = authJwtTokenUtil.generateToken(user);
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(authJwtTokenUtil.getExpiration() / 1000);

        // 保存令牌
        AuthenticationToken token = tokenDomainService.createJwtToken(
            userId,
            jwtToken,
            expiresAt
        );

        // 构建认证结果
        TokenInfo tokenInfo = TokenInfo.builder()
            .token(jwtToken)
            .tokenType("Bearer")
            .expiresIn(authJwtTokenUtil.getExpiration() / 1000)
            .build();

        return AuthenticationResult.success(user, tokenInfo);
    }

    /**
     * 验证令牌
     *
     * @param token     令牌
     * @param ipAddress IP地址
     * @param userAgent 用户代理
     * @return 认证结果
     */
    @Transactional
    public AuthenticationResult validateToken(String token, String ipAddress, String userAgent) {
        try {
            // 验证JWT令牌
            if (!authJwtTokenUtil.validateToken(token)) {
                return AuthenticationResult.failure("无效的令牌");
            }

            // 从JWT令牌中获取用户ID
            Long userId = authJwtTokenUtil.getUserIdFromToken(token);

            // 使用领域服务进行JWT认证
            Authentication authentication = authenticationDomainService.authenticateByJwtToken(
                userId,
                ipAddress,
                userAgent
            );

            // 检查认证结果
            if (!authentication.isSuccessful()) {
                return AuthenticationResult.failure(authentication.getFailureReason());
            }

            // 获取用户信息
            QingUser user = userRepository.findById(QingUserId.of(userId))
                .orElseThrow(() -> new AuthenticationException("用户不存在"));

            // 构建认证结果
            TokenInfo tokenInfo = TokenInfo.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(authJwtTokenUtil.getExpiration() / 1000)
                .build();

            return AuthenticationResult.success(user, tokenInfo);

        } catch (Exception e) {
            log.error("令牌验证失败", e);
            return AuthenticationResult.failure("令牌验证失败: " + e.getMessage());
        }
    }

    /**
     * 注销登录
     *
     * @param token 令牌
     */
    @Transactional
    public void logout(String token) {
        try {
            // 撤销令牌
            tokenDomainService.revokeToken(token, "用户注销");
        } catch (Exception e) {
            log.error("注销失败", e);
            throw new AuthenticationException("注销失败: " + e.getMessage());
        }
    }
}
