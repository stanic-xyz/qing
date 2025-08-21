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

package cn.chenyunlong.qing.auth.application.dto;

import cn.chenyunlong.qing.auth.domain.user.QingUser;
import lombok.Getter;

/**
 * 认证结果DTO
 *
 * @author 陈云龙
 */
@Getter
public class AuthenticationResult {

    /**
     * 是否成功
     */
    private final boolean success;

    /**
     * 用户信息
     */
    private final QingUser user;

    /**
     * 令牌信息
     */
    private final TokenInfo tokenInfo;

    /**
     * 失败原因
     */
    private final String failureReason;

    /**
     * 私有构造函数
     *
     * @param success       是否成功
     * @param user          用户信息
     * @param tokenInfo     令牌信息
     * @param failureReason 失败原因
     */
    private AuthenticationResult(boolean success, QingUser user, TokenInfo tokenInfo, String failureReason) {
        this.success = success;
        this.user = user;
        this.tokenInfo = tokenInfo;
        this.failureReason = failureReason;
    }

    /**
     * 创建成功结果
     *
     * @param user      用户信息
     * @param tokenInfo 令牌信息
     * @return 认证结果
     */
    public static AuthenticationResult success(QingUser user, TokenInfo tokenInfo) {
        return new AuthenticationResult(true, user, tokenInfo, null);
    }

    /**
     * 创建失败结果
     *
     * @param failureReason 失败原因
     * @return 认证结果
     */
    public static AuthenticationResult failure(String failureReason) {
        return new AuthenticationResult(false, null, null, failureReason);
    }
}
