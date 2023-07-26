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

package cn.chenyunlong.security.config.security.filter;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Stan
 */
public class MyAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private static final String TOKEN = "Authorization";
    private final static String AUTHORIZATION_HEADER = "Authorization";
    private final static String AUTHORIZATION_QUERY = "token";
    private final static String AUTHORIZATION_COOKIES = "qing-x-token";
    private final Logger logger = LoggerFactory.getLogger(MyAuthenticationProcessingFilter.class);


    public MyAuthenticationProcessingFilter() {
        super(new AntPathRequestMatcher("/login", "POST"));
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {
        //TODO 完善认证过程
        UsernamePasswordAuthenticationToken authRequest;
        authRequest = new UsernamePasswordAuthenticationToken("", "", null);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private String resolveToken(HttpServletRequest request) {
        String headerToken = request.getParameter(AUTHORIZATION_QUERY);
        if (StringUtils.hasText(headerToken)) {
            return headerToken;
        }
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        Optional<Cookie> zhangliToken =
                Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(AUTHORIZATION_COOKIES)).findFirst();
        return zhangliToken.map(Cookie::getValue).orElse(null);
    }

}