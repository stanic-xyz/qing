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

import cn.chenyunlong.qing.security.base.extension.DummyUserContextAware;
import cn.chenyunlong.qing.security.base.extension.UserContextAware;
import cn.chenyunlong.qing.security.config.security.MyJwtTokenFilter;
import cn.chenyunlong.qing.security.config.security.TokenProvider;
import cn.chenyunlong.qing.security.config.utils.JwtTokenUtil;
import cn.chenyunlong.qing.security.configures.authing.AuthingLoginConfigurer;
import cn.chenyunlong.qing.security.configures.authing.properties.AuthingProperties;
import cn.chenyunlong.qing.security.configures.my.QingLoginConfigurer;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.PathMatcher;

import java.util.List;

/**
 * spring security 配置类 自动注入JwtAuthenticationProvider
 * 此类主要给客户端使用 -> 客户端不再处理登录，只需要关注token 即可
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityCommonProperties.class)
public class SecurityConfig {

    private final AuthingProperties authingProperties;
    private final SecurityProperties securityProperties;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final AuthingLoginConfigurer authingLoginConfigurer;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final QingLoginConfigurer qingLoginConfigurer;
    private final SecurityContextRepository securityContextRepository;
    private final TokenProvider tokenProvider;

    @Bean
    @ConditionalOnMissingBean(UserContextAware.class)
    UserContextAware dummyUserContext() {
        return new DummyUserContextAware();
    }

    @Bean
    public SecurityFilterChain userLoginFilterChain(HttpSecurity http, PathMatcher mvcPathMatcher) throws Exception {
        http.formLogin(AbstractHttpConfigurer::disable);
        http.with(authingLoginConfigurer, authingLogin -> {

        });
        http.with(qingLoginConfigurer, qingLogin -> {
        });
        http.securityMatcher("/**")
            .authorizeHttpRequests(authorize -> {
                // 框架内部的白名单
                authorize.requestMatchers(authingProperties.getRedirectUrlPrefix(),
                    authingProperties.getAuthLoginUrlPrefix()).permitAll();
                List<String> whiteList = securityProperties.getWhiteList();
                if (CollUtil.isNotEmpty(whiteList)) {
                    RequestMatcher[] requestMatchers = new RequestMatcher[CollUtil.size(securityProperties.getWhiteList())];
                    for (int i = 0; i < whiteList.size(); i++) {
                        String pattern = whiteList.get(i);
                        RequestMatcher requestMatcher = new AntPathRequestMatcher(pattern);
                        requestMatchers[i] = requestMatcher;
                    }
                    authorize.requestMatchers(requestMatchers).permitAll();
                }

                authorize.anyRequest().authenticated();
            });

        http.exceptionHandling((exceptionHandling) -> {
                // 异常处理器
                exceptionHandling
                    .accessDeniedPage("/errors/access-denied")
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler);
            }
        );
        http.addFilterBefore(new MyJwtTokenFilter(tokenProvider, securityProperties), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
