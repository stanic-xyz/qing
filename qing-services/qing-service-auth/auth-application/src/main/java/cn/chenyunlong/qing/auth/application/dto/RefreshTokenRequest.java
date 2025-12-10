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

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * 刷新令牌请求类
 * 用于处理用户刷新访问令牌的请求
 */
@Data // Lombok注解，自动生成getter、setter、toString等方法
public class RefreshTokenRequest {


    /**
     * 刷新令牌
     * 用于获取新的访问令牌
     * 不能为空
     */
    @NotBlank(message = "刷新令牌不能为空")
    private String refreshToken;

}
