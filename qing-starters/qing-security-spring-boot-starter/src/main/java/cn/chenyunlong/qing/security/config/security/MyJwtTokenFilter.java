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

package cn.chenyunlong.qing.security.config.security;

import cn.chenyunlong.qing.security.config.SecurityProperties;
import cn.chenyunlong.qing.security.exception.IllegalTokenException;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class MyJwtTokenFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_QUERY = "token";
    private static final String AUTHORIZATION_COOKIES = "qing_token";
    private final TokenProvider tokenProvider;
    private final SecurityProperties securityProperties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public MyJwtTokenFilter(TokenProvider tokenProvider, SecurityProperties securityProperties) {
        this.tokenProvider = tokenProvider;
        this.securityProperties = securityProperties;
    }

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 白名单里面的直接跳过
        if (securityProperties.getWhiteList().stream().anyMatch(pathMatcher -> antPathMatcher.match(pathMatcher, request.getRequestURI()))) {
            filterChain.doFilter(request, response);
            return;
        }
        // 已经认证过的，直接放行
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwtToken = resolveToken(request);
        if (StrUtil.isNotBlank(jwtToken)) {
            // 从自定义tokenProvider中解析用户
            // 这里仍然是调用我们自定义的UserDetailsService，查库，检查用户名是否存在，
            // 如果是伪造的token,可能DB中就找不到username这个人了，抛出异常，认证失败
            if (this.tokenProvider.validateToken(jwtToken)) {
                try {
                    Authentication authentication = this.tokenProvider.getAuthentication(jwtToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (Exception exception) {
                    throw new IllegalTokenException("token认证失败，请重新登陆！");
                }
            } else {
                throw new BadCredentialsException("token非法！！");
            }
        }
        filterChain.doFilter(request, response);
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
        Optional<Cookie> optionalCookie =
            Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(AUTHORIZATION_COOKIES))
                .findFirst();
        return optionalCookie.map(Cookie::getValue).orElse(null);
    }
}
