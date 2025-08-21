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

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.qing.auth.domain.authentication.event.TokenCreated;
import cn.chenyunlong.qing.auth.domain.authentication.event.TokenRevoked;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 认证令牌实体
 * 表示一个有效的认证令牌
 *
 * @author 陈云龙
 */
@Getter
@Setter
public class AuthenticationToken extends BaseAggregate {

    @FieldDesc(name = "令牌值")
    private String tokenValue;

    @FieldDesc(name = "令牌类型")
    private TokenType tokenType;

    @FieldDesc(name = "关联用户ID")
    private AggregateId userId;

    @FieldDesc(name = "过期时间")
    private LocalDateTime expiresAt;

    @FieldDesc(name = "是否已撤销")
    private boolean revoked;

    @FieldDesc(name = "撤销时间")
    private LocalDateTime revokedAt;

    @FieldDesc(name = "撤销原因")
    private String revocationReason;

    /**
     * 创建认证令牌
     *
     * @param aggregateId 聚合根ID
     * @param tokenValue  令牌值
     * @param tokenType   令牌类型
     * @param userId      用户ID
     * @param expiresAt   过期时间
     * @return 认证令牌实例
     */
    public static AuthenticationToken create(AggregateId aggregateId,
                                           String tokenValue,
                                           TokenType tokenType,
                                           AggregateId userId,
                                           LocalDateTime expiresAt) {
        AuthenticationToken token = new AuthenticationToken();
        token.setId(aggregateId);
        token.setTokenValue(tokenValue);
        token.setTokenType(tokenType);
        token.setUserId(userId);
        token.setExpiresAt(expiresAt);
        token.setRevoked(false);
        
        // 发布令牌创建事件
        token.registerEvent(new TokenCreated(aggregateId, userId));
        
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
        this.revokedAt = LocalDateTime.now();
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
        return !revoked && LocalDateTime.now().isBefore(expiresAt);
    }
}