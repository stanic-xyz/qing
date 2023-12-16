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

package cn.chenyunlong.security.configures.password;

import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public final class PasswordLoginConfigurer
    extends AbstractHttpConfigurer<PasswordLoginConfigurer, HttpSecurity> {

    public static PasswordLoginConfigurer passLogin() {
        return new PasswordLoginConfigurer();
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        super.configure(httpSecurity);
        ApplicationContext context = httpSecurity.getSharedObject(ApplicationContext.class);
        httpSecurity.authenticationProvider(context.getBean(PasswordProvider.class));
        AuthenticationManagerBuilder builder =
            httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        httpSecurity.addFilterAfter(new PasswordLoginFilter(builder.getObject()),
            UsernamePasswordAuthenticationFilter.class);
    }

}
