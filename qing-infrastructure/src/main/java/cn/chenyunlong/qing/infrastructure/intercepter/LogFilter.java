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

package cn.chenyunlong.qing.infrastructure.intercepter;

import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter for logging.
 *
 * @author Stan
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 9)
public class LogFilter extends OncePerRequestFilter {


    /**
     * 排除敏感属性字段
     */
    public static final String[] EXCLUDE_PATH_PATTERS =
        new String[] {"/actuator/**", "/css/**", "/favicon.ico", "/file/**", "/img/**", "/js/**",
            "/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs", "/webjars/**"};

    /**
     * 执行控制器了
     *
     * @param request     请求信息
     * @param response    响应信息
     * @param filterChain 过滤器链
     * @throws ServletException 过滤过程中发生的异常
     * @throws IOException      io异常
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String remoteAddr = JakartaServletUtil.getClientIP(request);
        // Set start time
        long startTime = System.currentTimeMillis();
        // Do filter
        filterChain.doFilter(request, response);
        log.debug("path: {}, method: {}, ip: {}, status: {}, usage: {} ms",
            request.getServletPath(), request.getMethod(), remoteAddr, response.getStatus(),
            System.currentTimeMillis() - startTime);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        String servletPath = request.getServletPath();
        boolean shouldNotFilter = super.shouldNotFilter(request);
        if (!shouldNotFilter) {
            return Arrays.stream(EXCLUDE_PATH_PATTERS)
                .anyMatch(path -> new AntPathMatcher().match(path, servletPath));
        } else {
            return false;
        }
    }
}
