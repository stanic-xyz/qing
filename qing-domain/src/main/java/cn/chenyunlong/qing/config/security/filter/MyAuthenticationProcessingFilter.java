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

package cn.chenyunlong.qing.config.security.filter;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * 登录成功处理器过滤器。
 *
 * @author 陈云龙
 */
@Slf4j
public class MyAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private static final String TOKEN = "Authorization";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_QUERY = "token";
    private static final String AUTHORIZATION_COOKIES = "qing-x-token";


    public MyAuthenticationProcessingFilter() {
        super(new AntPathRequestMatcher("/login", "POST"));
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse)
        throws AuthenticationException {
        //TODO 完善认证过程
        UsernamePasswordAuthenticationToken authRequest;
        authRequest = new UsernamePasswordAuthenticationToken("", "", null);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 从请求信息中解析令牌。
     */
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
        Optional<Cookie> cookieOptional =
            Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(AUTHORIZATION_COOKIES))
                .findFirst();
        return cookieOptional.map(Cookie::getValue).orElse(null);
    }

}
