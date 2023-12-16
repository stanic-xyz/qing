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

package cn.chenyunlong.qing.config;

import cn.chenyunlong.security.config.security.configures.authing.properties.AuthingProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Authing 登录相关的配置
 *
 * @author 陈云龙
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AuthingProperties.class)
public class AuthingConfig implements InitializingBean {

    private final AuthingProperties authingProperties;

    @Override
    public void afterPropertiesSet() {
        log.info("Authing应用{{}}配置完毕！", authingProperties.getAppName());
    }
}
