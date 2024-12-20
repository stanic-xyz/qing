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

package cn.chenyunlong.qing.security.configures.authing;

import cn.chenyunlong.qing.security.configures.authing.properties.AuthingProperties;
import cn.chenyunlong.qing.security.service.UmsUserDetailsService;
import cn.chenyunlong.qing.security.signup.ConnectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableConfigurationProperties(AuthingProperties.class)
@RequiredArgsConstructor
public final class AuthingLoginConfigurer extends AbstractHttpConfigurer<AuthingLoginConfigurer, HttpSecurity>
    implements InitializingBean {

    private final AuthingProperties authingProperty;
    private final ConnectionService connectionService;
    private final UmsUserDetailsService userDetailsService;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final AuthingProperties authingProperties;

    @Override
    public void afterPropertiesSet() {
        log.info("Authing应用{{}}配置完毕！", authingProperty.getAppName());
    }

    @Override
    public void init(HttpSecurity builder) throws Exception {
        builder.csrf(AbstractHttpConfigurer::disable);
        //        builder.cors(AbstractHttpConfigurer::disable);
        super.init(builder);
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        super.configure(httpSecurity);
        AuthenticationManager authenticationManager = httpSecurity.getSharedObject(AuthenticationManager.class);
        AuthingLoginFilter authingLoginFilter = getAuthingLoginFilter(authenticationManager);
        AuthingProvider authingProvider = new AuthingProvider(authingProperty, userDetailsService, connectionService);
        httpSecurity.authenticationProvider(authingProvider);
        httpSecurity.addFilterBefore(authingLoginFilter, UsernamePasswordAuthenticationFilter.class);
    }

    private AuthingLoginFilter getAuthingLoginFilter(AuthenticationManager authenticationManager) {
        AuthingLoginFilter authingLoginFilter = new AuthingLoginFilter(authenticationManager, authingProperty);
        if (authenticationFailureHandler != null) {
            authingLoginFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        }
        if (authenticationSuccessHandler != null) {
            authingLoginFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        }
        authingLoginFilter.setRememberMeServices(new AuthingRememberMeServices(authingProperty));
        return authingLoginFilter;
    }

}
