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

package cn.chenyunlong.qing.auth.domain.authentication;

import cn.chenyunlong.qing.auth.domain.authentication.event.TokenCreated;
import cn.chenyunlong.qing.auth.domain.authentication.event.TokenRevoked;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.TokenId;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.TokenType;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import cn.chenyunlong.qing.domain.common.BaseSimpleBusinessEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * 认证令牌实体
 * 表示一个有效的认证令牌
 *
 * @author 陈云龙
 */
@Getter
@Setter
public class AuthenticationToken extends BaseSimpleBusinessEntity<TokenId> {

    // 令牌值，用于认证
    private String tokenValue;

    // 刷新令牌值，用于获取新的访问令牌
    private String refreshTokenValue;

    // 令牌类型，如访问令牌、刷新令牌等
    private TokenType tokenType;

    // 用户ID，标识令牌所属的用户
    private UserId userId;

    // 过期时间，令牌失效的时间点
    private Instant expiresAt;

    // 是否已撤销，标记令牌状态
    private boolean revoked;

    // 撤销时间，记录令牌被撤销的具体时间
    private Instant revokedAt;

    // 撤销原因，说明令牌被撤销的理由
    private String revocationReason;

    // 用户代理信息，记录客户端信息
    private String userAgent;

    /**
     * 创建认证令牌
     *
     * @param tokenId      聚合根ID
     * @param tokenValue   令牌值
     * @param tokenType    令牌类型
     * @param refreshToken 刷新令牌
     * @param userId       用户ID
     * @param expiresAt    过期时间
     * @param userAgent    用户代理信息
     * @return 认证令牌实例
     */
    public static AuthenticationToken create(TokenId tokenId,
                                             String tokenValue,
                                             TokenType tokenType,
                                             String refreshToken,
                                             UserId userId,
                                             Instant expiresAt,
                                             String userAgent) {
        AuthenticationToken token = new AuthenticationToken();
        token.setId(tokenId);
        token.setTokenValue(tokenValue);
        token.setTokenType(tokenType);
        token.setUserId(userId);
        token.setExpiresAt(expiresAt);
        token.setRevoked(false);
        token.setRefreshTokenValue(refreshToken);
        token.setUserAgent(userAgent);

        // 发布令牌创建事件
        token.registerEvent(new TokenCreated(tokenId, userId));

        return token;
    }

    /**
     * 撤销令牌
     *
     * @param reason 撤销原因
     */
    public void revoke(String reason) {
        if (this.revoked) {
            throw new IllegalStateException("令牌已被撤销");
        }

        this.revoked = true;
        this.revokedAt = Instant.now();
        this.revocationReason = reason;

        // 发布令牌撤销事件
        registerEvent(new TokenRevoked(this.getId(), this.getUserId(), reason));
    }

    /**
     * 检查令牌是否有效
     *
     * @return 是否有效
     */
    public boolean isValid() {
        return !revoked && Instant.now().isBefore(expiresAt);
    }
}
