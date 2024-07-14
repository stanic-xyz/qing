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

package cn.chenyunlong.qing.security.configures.my;

import cn.chenyunlong.qing.security.config.SecurityProperties;
import cn.chenyunlong.qing.security.configures.my.properties.MyLoginProperties;
import cn.chenyunlong.qing.security.service.UmsUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableConfigurationProperties(MyLoginProperties.class)
@RequiredArgsConstructor
public final class QingLoginConfigurer extends AbstractHttpConfigurer<QingLoginConfigurer, HttpSecurity> implements InitializingBean {

    private final SecurityProperties securityProperties;
    private final UmsUserDetailsService userDetailsService;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final SecurityContextRepository securityContextRepository;

    @Override
    public void afterPropertiesSet() {
        log.info("本地登录应用配置完毕：配置信息{}", securityProperties);
    }

    @Override
    public void init(HttpSecurity builder) throws Exception {
        builder.csrf(AbstractHttpConfigurer::disable);
        builder.cors(AbstractHttpConfigurer::disable);
        super.init(builder);
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        super.configure(httpSecurity);
        AuthenticationManager authenticationManager = httpSecurity.getSharedObject(AuthenticationManager.class);
        QingLoginFilter loginFilter = new QingLoginFilter(authenticationManager);
        loginFilter.setAuthenticationSuccessHandler(new MyAuthenticationSuccessHandler(securityProperties));
        loginFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        QingAuthenticationProvider authenticationProvider = new QingAuthenticationProvider(userDetailsService, new BCryptPasswordEncoder());
        httpSecurity.authenticationProvider(authenticationProvider);
        httpSecurity.securityContext(configurer -> configurer.securityContextRepository(securityContextRepository));
        httpSecurity.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.authenticationManager(authenticationManager);
    }
}
