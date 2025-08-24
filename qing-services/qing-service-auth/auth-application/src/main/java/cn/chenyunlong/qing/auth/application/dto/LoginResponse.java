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
import lombok.Data;

/**
 * 登录响应DTO
 *
 * @author 陈云龙
 */
@Data
public class LoginResponse {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 令牌信息
     */
    private TokenInfo tokenInfo;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 错误消息
     */
    private String errorMessage;

    /**
     * 从认证结果创建登录响应
     *
     * @param result 认证结果
     * @return 登录响应
     */
    public static LoginResponse fromAuthenticationResult(AuthenticationResult result) {
        LoginResponse response = new LoginResponse();
        response.setSuccess(result.isSuccess());

        if (result.isSuccess()) {
            QingUser user = result.getUser();
            response.setUserId(user.getId().getValue());
            response.setUsername(user.getUsername());
            response.setNickname(user.getNickname());
            response.setTokenInfo(result.getTokenInfo());
        } else {
            response.setErrorMessage(result.getFailureReason());
        }

        return response;
    }
}
