/*
 * Copyright (c) 2019-2022 YunLong Chen
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

package cn.chenyunlong.qing.infrastructure.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author stan
 */
@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "qing")
@EnableConfigurationProperties(value = {FileProperties.class, SecurityProperties.class, OssProperties.class})
public class QingProperties {

    /**
     * 是否开启aop日志
     */
    private boolean openAopLog = true;

    /**
     * 文件相关配置
     */
    private FileProperties file = new FileProperties();

    /**
     * 对象存储相关配置
     */
    private OssProperties oss = new OssProperties();

    /**
     * web安全配置
     */
    private SecurityProperties security = new SecurityProperties();

    /**
     * Swagger相关配置
     */
    private SwaggerProperties swagger = new SwaggerProperties();

    /**
     * 身份验证前缀
     */
    private String authenticationPrefix;

    /**
     * 缓存方式
     */
    private String cache;

    /**
     * 首页展示的数量
     */
    private int indexSize = 26;

    /**
     * 展示的搜索年份数量
     */
    private int yearCount = 10;

    /**
     * 邮件是否启用
     */
    private boolean isMailEnabled = false;

    /**
     * 登录超时女士
     */
    private long logTimeoutMs = 1000;
}

