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

package cn.chenyunlong.security.base;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * token 拦截器，加入上下文参数 user-agent ，也可以加入其它的扩展
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    public static final String HEADER_TOKEN_NAME = "token";
    public static final String USER_AGENT = "user-agent";
    private final static String LANCOO_TOKEN_PREFIX = "X-Token=";
    private final static String AUTHORIZATION_HEADER = "Authorization";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {
        String authorization = resolveToken(request);
        String userAgent = request.getHeader(USER_AGENT);
        List<AntPathRequestMatcher> list = Lists.newArrayList();
        list.add(new AntPathRequestMatcher("/auth/**"));
        list.add(new AntPathRequestMatcher("/public/**"));
        boolean match = false;
        for (AntPathRequestMatcher ant : list) {
            if (ant.matches(request)) {
                match = true;
            }
        }
        if (!Strings.isNullOrEmpty(authorization) && !match) {
            JwtAuthToken token = new JwtAuthToken(authorization);
            token.setUserAgent(userAgent);
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        chain.doFilter(request, response);
    }

    /**
     * 获取token
     * 集成了多种获取方式：
     * queryString:lg_tk
     * header：Authorization，X-Token=
     *
     * @param request request
     * @return token
     */
    private String resolveToken(@NonNull HttpServletRequest request) {

        //从query参数里面获取
        String tokenInParameters = request.getParameter("lg_tk");
        if (StringUtils.hasText(tokenInParameters)) {
            return tokenInParameters;
        }
        //从请求头里面获取
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(LANCOO_TOKEN_PREFIX)) {
            return bearerToken.substring(8);
        }
        if (StringUtils.hasText(bearerToken)) {
            return bearerToken;
        }
        //从cookie里面获取
        String tokenInCookies = getTokenFromCookies(request);
        if (StringUtils.hasText(tokenInCookies)) {
            return tokenInCookies;
        }
        return null;
    }

    /**
     * 从cookie里面获取token
     *
     * @param request 请求体
     * @return token中的cookie
     */
    private String getTokenFromCookies(@NonNull HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if ("lg_tk".equals(cookie.getName()) || "lg_name".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
