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

package cn.chenyunlong.qing.infrastructure.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * 环境配置。
 *
 * @author 陈云龙
 */
@Data
@Primary
@Component
@ConfigurationProperties(prefix = "qing")
public class QingProperties {

    /**
     * 是否开启aop日志
     */
    private boolean openAopLog = true;

    /**
     * Swagger相关配置
     */
    private DocProperties api;

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
    private boolean isMailEnabled = true;

    /**
     * 登录超时女士
     */
    private long logTimeoutMs = 1000;
}

