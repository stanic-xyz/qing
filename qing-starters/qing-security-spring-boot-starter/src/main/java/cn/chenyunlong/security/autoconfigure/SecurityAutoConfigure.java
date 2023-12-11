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

package cn.chenyunlong.security.autoconfigure;

import cn.chenyunlong.security.base.JwtAuthenticationEntryPoint;
import cn.chenyunlong.security.base.JwtAuthenticationTokenFilter;
import cn.chenyunlong.security.base.extension.DummyUserContextAware;
import cn.chenyunlong.security.base.extension.UserContextAware;
import cn.chenyunlong.security.config.SecurityCommonProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.web.cors.CorsUtils;

/**
 * spring security 配置类 自动注入JwtAuthenticationProvider
 * 此类主要给客户端使用 -> 客户端不再处理登录，只需要关注token 即可
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityCommonProperties.class)
public class SecurityAutoConfigure {

    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    private final SecurityCommonProperties commonProperties;

    @Bean
    @ConditionalOnMissingBean(UserContextAware.class)
    UserContextAware dummyUserContext() {
        return new DummyUserContextAware();
    }

    /**
     * 配置Security过滤器链。
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .requiresChannel()
            .anyRequest()
            .requiresInsecure()
            .and()
            .csrf()
            .disable()//csrf取消
            .cors()
            .disable()
            .exceptionHandling()
            .authenticationEntryPoint(unauthorizedHandler)
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()//不再存储session
            .headers()
            .frameOptions()
            .disable()
            .and()
            .headers()
            .addHeaderWriter(new XFrameOptionsHeaderWriter(
                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
            .and()
            .authorizeHttpRequests()
            .requestMatchers(CorsUtils::isPreFlightRequest)
            .permitAll()
            //OPTIONS请求直接放行
            .requestMatchers(HttpMethod.OPTIONS)
            .permitAll()
            //静态资源直接放行
            .requestMatchers(HttpMethod.GET, "/", "/*.html", "/favicon.ico", "/**/*.html",
                    "/**/*.css", "/**/*.js", "/auth/**")
            .permitAll()
            //Swagger相关直接放行
            .requestMatchers("/swagger-ui.html", "/swagger-resources/**", "/webjars/**",
                "/v2/api-docs", "/configuration/ui", "/configuration/security", "/auth/**",
                "/public/**")
            .permitAll()
            .requestMatchers(commonProperties.getUnAuthUrls().toArray(new String[0]))
            .permitAll()
            .requestMatchers("/admin/**)")
            .hasRole("ADMIN")
            .anyRequest()
            .authenticated();

        httpSecurity.addFilterBefore(authenticationTokenFilterBean(),
            UsernamePasswordAuthenticationFilter.class);
        // 资源创建
        httpSecurity.headers().cacheControl();

        return httpSecurity.build();
    }

    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() {
        return new JwtAuthenticationTokenFilter();
    }

}
