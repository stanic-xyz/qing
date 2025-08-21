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

/**
 * 认证类型枚举
 *
 * @author 陈云龙
 */
public enum AuthenticationType {
    
    /**
     * 用户名密码认证
     */
    USERNAME_PASSWORD,
    
    /**
     * 手机验证码认证
     */
    MOBILE_CODE,
    
    /**
     * 邮箱验证码认证
     */
    EMAIL_CODE,
    
    /**
     * 第三方认证（OAuth2）
     */
    OAUTH2,
    
    /**
     * JWT令牌认证
     */
    JWT_TOKEN
}