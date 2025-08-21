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

import cn.chenyunlong.qing.auth.domain.authentication.AuthenticationToken;
import cn.chenyunlong.qing.auth.domain.authentication.TokenType;
import cn.chenyunlong.qing.auth.domain.authentication.exception.AuthenticationException;
import cn.chenyunlong.qing.auth.domain.authentication.repository.TokenRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 令牌领域服务
 * 负责处理令牌相关的领域逻辑
 *
 * @author 陈云龙
 */
@Service
@RequiredArgsConstructor
public class TokenDomainService {

    private final TokenRepository tokenRepository;

    /**
     * 创建JWT令牌
     *
     * @param userId    用户ID
     * @param tokenValue JWT令牌值
     * @param expiresAt 过期时间
     * @return 创建的令牌
     */
    public AuthenticationToken createJwtToken(AggregateId userId, String tokenValue, LocalDateTime expiresAt) {
        // 创建令牌实体
        AuthenticationToken token = AuthenticationToken.create(
            new AggregateId(IdUtil.getSnowflakeNextId()),
            tokenValue,
            TokenType.JWT,
            userId,
            expiresAt
        );
        
        // 保存令牌
        return tokenRepository.save(token);
    }

    /**
     * 验证令牌
     *
     * @param tokenValue 令牌值
     * @return 令牌对象
     * @throws AuthenticationException 如果令牌无效或已过期
     */
    public AuthenticationToken validateToken(String tokenValue) {
        // 查找令牌
        Optional<AuthenticationToken> tokenOpt = tokenRepository.findByTokenValue(tokenValue);
        if (tokenOpt.isEmpty()) {
            throw new AuthenticationException("令牌不存在");
        }
        
        AuthenticationToken token = tokenOpt.get();
        
        // 检查令牌是否有效
        if (!token.isValid()) {
            if (token.isRevoked()) {
                throw new AuthenticationException("令牌已被撤销: " + token.getRevocationReason());
            } else {
                throw new AuthenticationException("令牌已过期");
            }
        }
        
        return token;
    }

    /**
     * 撤销令牌
     *
     * @param tokenValue 令牌值
     * @param reason     撤销原因
     * @throws AuthenticationException 如果令牌不存在
     */
    public void revokeToken(String tokenValue, String reason) {
        // 查找令牌
        Optional<AuthenticationToken> tokenOpt = tokenRepository.findByTokenValue(tokenValue);
        if (tokenOpt.isEmpty()) {
            throw new AuthenticationException("令牌不存在");
        }
        
        AuthenticationToken token = tokenOpt.get();
        
        // 撤销令牌
        token.revoke(reason);
        tokenRepository.save(token);
    }

    /**
     * 撤销用户的所有令牌
     *
     * @param userId 用户ID
     * @param reason 撤销原因
     * @return 撤销的令牌数量
     */
    public int revokeAllUserTokens(AggregateId userId, String reason) {
        return tokenRepository.revokeAllTokensByUserId(userId, reason);
    }

    /**
     * 获取用户的有效令牌列表
     *
     * @param userId 用户ID
     * @return 令牌列表
     */
    public List<AuthenticationToken> getUserValidTokens(AggregateId userId) {
        return tokenRepository.findValidTokensByUserId(userId);
    }
}