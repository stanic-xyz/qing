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
import cn.chenyunlong.qing.auth.domain.authentication.event.AuthenticationFailed;
import cn.chenyunlong.qing.auth.domain.authentication.event.AuthenticationSucceeded;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.AuthFailureReason;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.AuthenticationId;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.AuthenticationType;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.IpAddress;
import cn.chenyunlong.qing.auth.domain.user.User;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import cn.chenyunlong.qing.domain.common.BaseSimpleBusinessEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 认证聚合根
 * 表示一次认证过程
 *
 * @author 陈云龙
 */
@Getter
@Setter
public class Authentication extends BaseSimpleBusinessEntity<AuthenticationId> {

    @FieldDesc(name = "认证类型")
    private AuthenticationType type;

    @FieldDesc(name = "认证主体")
    private String principal;

    @FieldDesc(name = "认证凭证")
    private String credentials;

    @FieldDesc(name = "认证状态")
    private AuthenticationStatus status;

    @FieldDesc(name = "认证时间")
    private LocalDateTime authenticatedAt;

    @FieldDesc(name = "认证IP")
    private String ipAddress;

    @FieldDesc(name = "用户代理")
    private String userAgent;

    @FieldDesc(name = "关联用户ID")
    private UserId userId;

    @FieldDesc(description = "认证是否成功")
    private boolean successful;

    @FieldDesc(description = "失败原因")
    private AuthFailureReason failureReason;

    @FieldDesc(description = "失败原因详情")
    private String failureDetails;

    private IpAddress clientIpAddress;

    private AuthenticationToken tokenInfo;

    /**
     * 创建认证实例
     *
     * @param authenticationId 聚合根ID
     * @param type             认证类型
     * @param principal        认证主体
     * @param credentials      认证凭证
     * @param ipAddress        IP地址
     * @param userAgent        用户代理
     * @return 认证实例
     */
    public static Authentication create(AuthenticationId authenticationId,
                                        AuthenticationType type,
                                        String principal,
                                        String credentials,
                                        String ipAddress,
                                        String userAgent) {
        Authentication authentication = new Authentication();
        authentication.setId(authenticationId);
        authentication.setType(type);
        authentication.setPrincipal(principal);
        authentication.setCredentials(credentials);
        authentication.setStatus(AuthenticationStatus.PENDING);
        authentication.setIpAddress(ipAddress);
        authentication.setUserAgent(userAgent);
        return authentication;
    }

    public static Authentication createForUser(User user, IpAddress clientIpAddress) {

        Authentication authentication = new Authentication();

        authentication.setId(AuthenticationId.generate());
        authentication.setAuthenticatedAt(LocalDateTime.now());
        authentication.setUserId(user.getId());
        authentication.setClientIpAddress(clientIpAddress);

        return authentication;
    }

    public static Authentication createFailed(User user, IpAddress clientIpAddress, String userAgent, String message) {
        return create(AuthenticationId.generate(), AuthenticationType.USERNAME_PASSWORD, user.getUsername().value(), user.getEncodedPassword().value(), clientIpAddress.getValue(), userAgent);
    }

    /**
     * 认证成功
     *
     * @param user 认证用户
     */
    public void succeed(User user) {
        if (this.status != AuthenticationStatus.PENDING) {
            throw new IllegalStateException("认证已完成，不能重复认证");
        }

        this.status = AuthenticationStatus.SUCCESS;
        this.userId = user.getId();
        this.authenticatedAt = LocalDateTime.now();

        // 发布认证成功事件
        registerEvent(new AuthenticationSucceeded(this.getId(), user.getId()));
    }

    /**
     * 认证失败
     *
     * @param reason 失败原因
     */
    public void fail(String reason) {
        if (this.status != AuthenticationStatus.PENDING) {
            throw new IllegalStateException("认证已完成，不能重复认证");
        }

        this.status = AuthenticationStatus.FAILED;
        this.authenticatedAt = LocalDateTime.now();

        // 发布认证失败事件
        registerEvent(new AuthenticationFailed(this.getId(), reason));
    }

    /**
     * 认证失败
     *
     * @param authFailureReason 失败原因
     */
    public void fail(AuthFailureReason authFailureReason) {
        fail(authFailureReason.toString());
    }
}
