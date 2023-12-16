/*
 * Copyright (c) 2023  YunLong Chen
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

package cn.chenyunlong.security.config.security.configures.authing;

import cn.authing.sdk.java.dto.authentication.OIDCTokenResponse;
import cn.chenyunlong.common.model.ApiResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public final class AuthingLoginConfigurer
    extends AbstractHttpConfigurer<AuthingLoginConfigurer, HttpSecurity> {

    public static AuthingLoginConfigurer authingLogin() {
        return new AuthingLoginConfigurer();
    }

    @Override
    public void init(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .cors()
            .and()
            .csrf()
            .disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        super.configure(httpSecurity);
        ApplicationContext context = httpSecurity.getSharedObject(ApplicationContext.class);
        httpSecurity.authenticationProvider(context.getBean(AuthingProvider.class));
        AuthenticationManagerBuilder authenticationManagerBuilder =
            httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        AuthingLoginFilter authingLoginFilter =
            new AuthingLoginFilter(authenticationManagerBuilder.getObject());
        authingLoginFilter.setAuthenticationFailureHandler(
                (request, response, exception) -> System.out.println("AuthingLoginConfigurer.onAuthenticationFailure"));
        authingLoginFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            AuthingToken authingToken = (AuthingToken) authentication;
            //构建一个Token
            ApiResult<OIDCTokenResponse> success = ApiResult.success(authingToken.getResponse());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(success));
        });
        httpSecurity.addFilterAfter(authingLoginFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
