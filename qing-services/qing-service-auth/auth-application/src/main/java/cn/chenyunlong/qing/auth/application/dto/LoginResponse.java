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
import lombok.Builder;
import lombok.Data;

/**
 * 登录响应DTO
 *
 * @author 陈云龙
 */
@Data
@Builder
public class LoginResponse {

    private String scope;
    private String accessToken;
    private String idToken;
    private String refreshToken;
    private String tokenType;
    private String expireIn;
    private String code;

    /**
     * 从认证结果创建登录响应
     *
     * @param result 认证结果
     * @return 登录响应
     */
    public static LoginResponse fromAuthenticationResult(AuthenticationResultDTO result) {
        AuthenticationToken authenticationToken = result.tokenInfo();
        return LoginResponse.builder()
                .scope("accessToken")
                .accessToken(authenticationToken.getTokenValue())
                .refreshToken(authenticationToken.getRefreshTokenValue())
                .tokenType(authenticationToken.getTokenType().name())
                .expireIn(String.valueOf(authenticationToken.getExpiresAt()))
                .idToken("idToken")
                .code("code")
                .build();
    }
}
