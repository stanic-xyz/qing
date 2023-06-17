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

package cn.chenyunlong.qing.infrastructure.security.config;

import cn.chenyunlong.qing.infrastructure.security.configures.my.MyAccessDeniedHandler;
import cn.chenyunlong.qing.infrastructure.security.configures.my.MyAuthenticationEntryPoint;
import cn.chenyunlong.qing.infrastructure.security.support.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static cn.chenyunlong.qing.infrastructure.security.configures.authing.AuthingLoginConfigurer.authingLogin;

/**
 * @author Stan
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final MyAccessDeniedHandler myAccessDeniedHandler;
    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;
    private final MyAuthenticationEntryPoint myAuthenticationEntryPoint;


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User
                // TODO 测试阶段暂时使用这个来做登录
                .withDefaultPasswordEncoder().username("user").password("password").roles("USER").build());

        return httpSecurity
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(manager)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    /**
     * 集成Authing的登录认证服务
     */
    @Bean
    public SecurityFilterChain authingLoginFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .cors()
                .and()
                .csrf()
                .disable()
                // 其他相关的处理器
                .exceptionHandling()
                .authenticationEntryPoint(myAuthenticationEntryPoint)
                .accessDeniedHandler(myAccessDeniedHandler)
                .and()
                // 取消表单登录
                .formLogin()
                .disable()
                // 管理认证
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers("/actuator/**", "/authorize/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**")
                            .permitAll()
                            .requestMatchers("/easyui/**", "/detail/**")
                            .permitAll();
                    authorize
                            .requestMatchers("/actuator/**", "/authorize/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**")
                            .permitAll()
                            .requestMatchers("/js/**", "/css/**", "/img/*")
                            .permitAll()
                            .requestMatchers("/login")
                            .permitAll()
                            .requestMatchers("/static/**")
                            .permitAll()
                            .requestMatchers("/favicon.ico")
                            .permitAll()
                            .requestMatchers("/natcross/**")
                            .permitAll()
                            .requestMatchers("/management/**")
                            .permitAll();
                    authorize.anyRequest().permitAll();
                });

        httpSecurity.apply(authingLogin());
        return httpSecurity.build();
    }

}
