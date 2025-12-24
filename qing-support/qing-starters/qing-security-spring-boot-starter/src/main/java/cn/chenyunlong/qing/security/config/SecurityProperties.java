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

package cn.chenyunlong.qing.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Security配置。
 *
 * @author 陈云龙
 */
@Data
@Component
@ConfigurationProperties(prefix = "qing.security")
public class SecurityProperties {

    /**
     * 匿名访问地址
     */
    private String anonUrl = "";

    /**
     * 默认生成的账户密码
     */
    private String defaultPassword = "123456";

    /**
     * token有效时间  默认1天
     */
    private long jwtTimeOutSecond = TimeUnit.DAYS.toSeconds(1);

    /**
     * 令牌自定义标识
     */
    private String tokenHeader = HttpHeaders.AUTHORIZATION;

    /**
     * 安全密钥
     */
    private String secretKey = "Stanic";
    /**
     * 一般密钥过期时间
     */
    private long tokenValidityInMilliseconds = 24 * 60 * 1000;
    /**
     * “记住我”密钥过期时间
     */
    private long tokenValidityInMillisecondsForRememberMe = 30 * 24 * 60 * 1000;

    /**
     * 白名单
     */
    private List<String> whiteList;
}
