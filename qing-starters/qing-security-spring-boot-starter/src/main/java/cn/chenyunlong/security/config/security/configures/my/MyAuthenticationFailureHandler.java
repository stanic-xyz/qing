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

package cn.chenyunlong.security.config.security.configures.my;

import cn.chenyunlong.common.model.ApiResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * 任务失败处理器
 *
 * @author 陈云龙
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        ApiResult<String> result;
        if (exception instanceof UsernameNotFoundException
            || exception instanceof BadCredentialsException) {
            result = ApiResult.fail(exception.getMessage());
        } else if (exception instanceof LockedException) {
            result = ApiResult.fail("账户被锁定，请联系管理员!");
        } else if (exception instanceof CredentialsExpiredException) {
            result = ApiResult.fail("证书过期，请联系管理员!");
        } else if (exception instanceof AccountExpiredException) {
            result = ApiResult.fail("账户过期，请联系管理员!");
        } else if (exception instanceof DisabledException) {
            result = ApiResult.fail("账户被禁用，请联系管理员!");
        } else {
            result = ApiResult.fail("登录失败!");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
