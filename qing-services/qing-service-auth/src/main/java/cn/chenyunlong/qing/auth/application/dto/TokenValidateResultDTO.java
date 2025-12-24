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

import cn.chenyunlong.qing.auth.domain.authentication.valueObject.AuthenticationResult;
import lombok.Data;

/**
 * 认证结果DTO
 *
 * @author 陈云龙
 */
@Data
public final class TokenValidateResultDTO {
    private final boolean success;
    private final String reason;


    /**
     * 私有构造函数
     *
     * @param success 是否成功
     */
    public TokenValidateResultDTO(boolean success, String reason) {
        this.success = success;
        this.reason = reason;
    }

    /**
     * 创建成功结果
     *
     * @return 认证结果
     */
    public static TokenValidateResultDTO success() {
        return new TokenValidateResultDTO(true, null);
    }

    /**
     * 创建失败结果
     *
     * @param failureReason 失败原因
     * @return 认证结果
     */
    public static TokenValidateResultDTO failure(String failureReason) {
        return new TokenValidateResultDTO(false, failureReason);
    }

    public static TokenValidateResultDTO from(AuthenticationResult authenticationResult) {
        return new TokenValidateResultDTO(authenticationResult.isSuccess(), authenticationResult.getMessage());
    }

}
