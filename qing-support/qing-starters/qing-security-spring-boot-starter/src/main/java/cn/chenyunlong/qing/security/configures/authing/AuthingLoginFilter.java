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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class AuthingLoginFilter extends AbstractAuthenticationProcessingFilter {

    public AuthingLoginFilter(AuthenticationManager authenticationManager, AuthingProperties authingProperties) {
        super(AntPathRequestMatcher.antMatcher(HttpMethod.GET, authingProperties.getAuthLoginUrlPrefix()), authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        AuthingLoginRequest loginRequest = new AuthingLoginRequest();
        loginRequest.setCode(request.getParameter("code"));
        loginRequest.setState(request.getParameter("state"));
        AuthingLoginToken passToken = new AuthingLoginToken(loginRequest);
        return this.getAuthenticationManager().authenticate(passToken);
    }
}
