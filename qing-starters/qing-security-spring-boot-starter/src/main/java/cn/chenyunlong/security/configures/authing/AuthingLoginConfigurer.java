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

package cn.chenyunlong.security.configures.authing;

import cn.authing.sdk.java.dto.authentication.OIDCTokenResponse;
import cn.chenyunlong.common.model.ApiResult;
import cn.chenyunlong.security.configures.authing.properties.AuthingProperties;
import cn.chenyunlong.security.signup.ConnectionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@EnableConfigurationProperties(AuthingProperties.class)
@AutoConfigureAfter(AuthenticationManager.class)
@RequiredArgsConstructor
public final class AuthingLoginConfigurer extends AbstractHttpConfigurer<AuthingLoginConfigurer, HttpSecurity> implements InitializingBean {

    private final AuthingProperties authingProperty;
    private final ConnectionService connectionService;
    private final UserDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        super.configure(httpSecurity);
        AuthenticationManager authenticationManager = httpSecurity.getSharedObject(AuthenticationManager.class);
        AuthingLoginFilter authingLoginFilter = new AuthingLoginFilter(authenticationManager, authingProperty);
        authingLoginFilter.setAuthenticationFailureHandler((request, response, exception) -> System.out.println("AuthingLoginConfigurer.onAuthenticationFailure"));
        authingLoginFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            AuthingLoginToken authingLoginToken = (AuthingLoginToken) authentication;
            //构建一个Token
            ApiResult<OIDCTokenResponse> success = ApiResult.success(authingLoginToken.getResponse());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(success));
        });
        authingLoginFilter.setRememberMeServices(new AuthingRememberMeServices(authingProperty));
        httpSecurity.authenticationProvider(new AuthingProvider(authingProperty, userDetailsService, connectionService));
        httpSecurity.addFilterBefore(authingLoginFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Authing应用{{}}配置完毕！", authingProperty.getAppName());
    }
}
