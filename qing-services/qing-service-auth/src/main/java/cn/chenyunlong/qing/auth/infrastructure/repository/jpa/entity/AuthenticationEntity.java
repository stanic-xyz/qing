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

package cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository.entiry;

import cn.chenyunlong.jpa.support.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 认证实体
 *
 * @author 陈云龙
 */
@Data
@Entity
@Table(name = "auth_authentication")
@EqualsAndHashCode(callSuper = true)
public class AuthenticationEntity extends BaseJpaEntity {

    /**
     * 认证类型
     */
    @Column(name = "authentication_type", nullable = false, length = 50)
    private String authenticationType;

    /**
     * 认证主体
     */
    @Column(name = "principal", nullable = false, length = 255)
    private String principal;

    /**
     * 认证凭证
     */
    @Column(name = "credentials", length = 1024)
    private String credentials;

    /**
     * 认证状态
     */
    @Column(name = "status", nullable = false, length = 50)
    private String status;

    /**
     * 认证时间
     */
    @Column(name = "authentication_time")
    private LocalDateTime authenticationTime;

    /**
     * IP地址
     */
    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    /**
     * 用户代理
     */
    @Column(name = "user_agent", length = 1024)
    private String userAgent;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 失败原因
     */
    @Column(name = "failure_reason", length = 1024)
    private String failureReason;
}
