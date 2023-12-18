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

package cn.chenyunlong.qing.config.security;

import cn.chenyunlong.security.base.extension.DummyUserContextAware;
import cn.chenyunlong.security.base.extension.UserContextAware;
import cn.chenyunlong.security.config.SecurityCommonProperties;
import cn.chenyunlong.security.configures.authing.AuthingLoginConfigurer;
import cn.chenyunlong.security.configures.authing.properties.AuthingProperties;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * spring security 配置类 自动注入JwtAuthenticationProvider
 * 此类主要给客户端使用 -> 客户端不再处理登录，只需要关注token 即可
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityCommonProperties.class)
public class SecurityConfig {

    @Resource
    private AuthingProperties authingProperties;
    @Resource
    private AuthingLoginConfigurer authingLoginConfigurer;

    @Bean
    @ConditionalOnMissingBean(UserContextAware.class)
    UserContextAware dummyUserContext() {
        return new DummyUserContextAware();
    }

    @Bean
    public SecurityFilterChain userLoginFilterChain(HttpSecurity http) throws Exception {
        http.formLogin().loginPage("/login.html").defaultSuccessUrl("/index.html");
        http.logout().logoutSuccessUrl("/login.html");
        http.apply(this.authingLoginConfigurer);
        http.csrf().disable();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET, authingProperties.getRedirectUrlPrefix() + "*", authingProperties.getRedirectUrlPrefix() + "*").permitAll();
        http.authorizeHttpRequests().anyRequest().permitAll();
        return http.build();
    }

}
