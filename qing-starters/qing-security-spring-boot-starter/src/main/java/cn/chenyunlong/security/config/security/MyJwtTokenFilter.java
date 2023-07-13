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

package cn.chenyunlong.security.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class MyJwtTokenFilter extends AbstractAuthenticationProcessingFilter {

    private final static String AUTHORIZATION_HEADER = "Authorization";
    private final static String AUTHORIZATION_QUERY = "token";
    private final static String AUTHORIZATION_COOKIES = "qing_token";
    private final TokenProvider tokenProvider;

    public MyJwtTokenFilter(String defaultFilterProcessesUrl, TokenProvider tokenProvider) {
        super(defaultFilterProcessesUrl);
        this.tokenProvider = tokenProvider;
    }


    /**
     * @param request  from which to extract parameters and perform the authentication
     * @param response the response, which may be needed if the implementation has to do a
     *                 redirect as part of a multi-stage authentication process (such as OIDC).
     * @return
     * @throws AuthenticationException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String jwt = resolveToken(request);
        if (StringUtils.hasText(jwt)) {
            // 从自定义tokenProvider中解析用户
            // 这里仍然是调用我们自定义的UserDetailsService，查库，检查用户名是否存在，
            // 如果是伪造的token,可能DB中就找不到username这个人了，抛出异常，认证失败
            if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
                try {
                    Authentication authentication = this.tokenProvider.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    return this.getAuthenticationManager().authenticate(authentication);
                } catch (Exception exp) {
                    logger.info("token验证错误！");
                }
            }
        }
        return null;
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
                Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(AUTHORIZATION_COOKIES)).findFirst();
        return optionalCookie.map(Cookie::getValue).orElse(null);
    }
}
