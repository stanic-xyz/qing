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

package cn.chenyunlong.qing.infrastructure.security.config;

import cn.chenyunlong.qing.infrastructure.config.properties.QingProperties;
import cn.chenyunlong.qing.infrastructure.security.MyAccessDeniedHandler;
import cn.chenyunlong.qing.infrastructure.security.MyAuthenticationEntryPoint;
import cn.chenyunlong.qing.infrastructure.security.filter.MyTokenFilter;
import cn.chenyunlong.qing.infrastructure.security.support.TokenProvider;
import cn.chenyunlong.qing.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Stan
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MyAccessDeniedHandler myAccessDeniedHandler;
    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;
    private final MyAuthenticationEntryPoint myAuthenticationEntryPoint;


    public SecurityConfig(AuthenticationSuccessHandler authenticationSuccessHandler,
                          AuthenticationFailureHandler authenticationFeilureHandler,
                          QingProperties qingProperties,
                          MyAccessDeniedHandler myAccessDeniedHandler,
                          UserService userService,
                          TokenProvider tokenProvider,
                          UserDetailsService myUserDetailService,
                          MyAuthenticationEntryPoint myAuthenticationEntryPoint) {
        this.myAccessDeniedHandler = myAccessDeniedHandler;
        this.tokenProvider = tokenProvider;
        this.userDetailsService = myUserDetailService;
        this.myAuthenticationEntryPoint = myAuthenticationEntryPoint;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //禁用CSRF 开启跨域
        http
                .cors()
                .and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(myAuthenticationEntryPoint)
                .accessDeniedHandler(myAccessDeniedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/actuator/**", "/authorize/**", "/file/**", "/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs", "/webjars/**").permitAll()
                .antMatchers("/easyui/**", "detail/**").permitAll()
                .antMatchers("/js/**", "/css/**", "/img/*").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/natcross/**").permitAll()
                .antMatchers("/management/**").permitAll()
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(new MyTokenFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
    }
}
