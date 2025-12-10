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

package cn.chenyunlong.qing.auth.application.dto;

import cn.chenyunlong.qing.auth.domain.authentication.AuthenticationToken;
import cn.chenyunlong.qing.auth.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;

/**
 * 认证结果DTO
 *
 * @param success       是否成功
 * @param tokenInfo     令牌信息
 * @param failureReason 失败原因
 * @author 陈云龙
 */
@Builder(access = AccessLevel.PRIVATE)
public record AuthenticationResultDTO(boolean success, AuthenticationToken tokenInfo, String failureReason, User user) {

    /**
     * 创建成功结果
     *
     * @param authenticationToken 令牌信息
     * @return 认证结果
     */
    public static AuthenticationResultDTO success(AuthenticationToken authenticationToken, User user) {
        return AuthenticationResultDTO.builder()
                .success(true)
                .tokenInfo(authenticationToken)
                .user(user)
                .build();
    }

    /**
     * 创建失败结果
     *
     * @param failureReason 失败原因
     * @return 认证结果
     */
    public static AuthenticationResultDTO failure(String failureReason) {
        return new AuthenticationResultDTO(false, null, failureReason, null);
    }

}
