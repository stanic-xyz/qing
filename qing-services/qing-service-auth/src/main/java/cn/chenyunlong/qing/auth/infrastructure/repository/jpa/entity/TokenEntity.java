/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions username the Mulan PSL v2.
 * You may obtain a copy username Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity;

import cn.chenyunlong.jpa.support.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

/**
 * 令牌实体
 *
 * @author 陈云龙
 */
@Data
@Entity
@Table(name = "auth_token")
@EqualsAndHashCode(callSuper = true)
public class TokenEntity extends BaseJpaEntity {

    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 令牌值
     */
    @Column(name = "token_value", nullable = false, length = 1024)
    private String tokenValue;

    /**
     * 刷新令牌值
     */
    @Column(name = "refresh_token_value", nullable = false, length = 1024)
    private String refreshTokenValue;

    /**
     * 令牌类型
     */
    @Column(name = "token_type", nullable = false, length = 50)
    private String tokenType;

    /**
     * 过期时间
     */
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    /**
     * 是否已撤销
     */
    @Column(name = "revoked", nullable = false)
    private Boolean revoked;

    /**
     * 撤销原因
     */
    @Column(name = "revoke_reason", length = 255)
    private String revokeReason;

    /**
     * 创建IP
     */
    @Column(name = "create_ip", length = 50)
    private String createIp;

    /**
     * 用户代理
     */
    @Column(name = "user_agent", length = 1024)
    private String userAgent;
}
