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

import lombok.Getter;

/**
 * 令牌信息DTO
 *
 * @author 陈云龙
 */
@Getter
public class TokenInfo {

    /**
     * 令牌值
     */
    private final String token;

    /**
     * 令牌类型
     */
    private final String tokenType;

    /**
     * 过期时间（秒）
     */
    private final long expiresIn;

    /**
     * 私有构造函数
     *
     * @param token     令牌值
     * @param tokenType 令牌类型
     * @param expiresIn 过期时间（秒）
     */
    private TokenInfo(String token, String tokenType, long expiresIn) {
        this.token = token;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    /**
     * 创建构建器
     *
     * @return 构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 令牌信息构建器
     */
    public static class Builder {
        private String token;
        private String tokenType;
        private long expiresIn;

        /**
         * 设置令牌值
         *
         * @param token 令牌值
         * @return 构建器
         */
        public Builder token(String token) {
            this.token = token;
            return this;
        }

        /**
         * 设置令牌类型
         *
         * @param tokenType 令牌类型
         * @return 构建器
         */
        public Builder tokenType(String tokenType) {
            this.tokenType = tokenType;
            return this;
        }

        /**
         * 设置过期时间（秒）
         *
         * @param expiresIn 过期时间（秒）
         * @return 构建器
         */
        public Builder expiresIn(long expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }

        /**
         * 构建令牌信息
         *
         * @return 令牌信息
         */
        public TokenInfo build() {
            return new TokenInfo(token, tokenType, expiresIn);
        }
    }
}
