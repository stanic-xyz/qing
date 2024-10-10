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

package cn.chenyunlong.qing.security.base;

import cn.chenyunlong.qing.security.config.AuthErrorMsg;
import cn.chenyunlong.qing.security.exception.MethodNotSupportException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

/**
 * 通用登录处理器。
 */
@Getter
public abstract class BaseLoginProcessFilter extends AbstractAuthenticationProcessingFilter {
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final ObjectMapper mapper;

    protected BaseLoginProcessFilter(String defaultFilterProcessesUrl,
                                     AuthenticationSuccessHandler successHandler,
                                     AuthenticationFailureHandler failureHandler,
                                     ObjectMapper mapper) {
        super(defaultFilterProcessesUrl);
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.mapper = mapper;
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain,
                                            Authentication authResult)
        throws IOException, ServletException {
        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
        throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

    /**
     * 检查登录方法。
     */
    public void checkMethod(HttpServletRequest request) {
        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            throw new MethodNotSupportException(AuthErrorMsg.methodNotSupport.getName());
        }
    }
}
