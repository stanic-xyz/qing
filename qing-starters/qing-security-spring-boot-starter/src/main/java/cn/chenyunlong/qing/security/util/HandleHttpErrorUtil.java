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

package cn.chenyunlong.qing.security.util;

import cn.chenyunlong.qing.security.exception.CustomAuthenticationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import java.io.Serializable;

@Slf4j
public class HandleHttpErrorUtil {

    private static final Integer AUTH_ERROR_CODE = 403;

    private HandleHttpErrorUtil() {
    }

    /**
     * 处理 Http 错误。
     */
    public static void handleHttpError(HttpServletResponse response,
                                       AuthenticationException message) throws Exception {
        //设置http 返回请求错误码
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        AuthResponse authResponse = new AuthResponse();
        if (CustomAuthenticationException.class.isAssignableFrom(message.getClass())) {
            CustomAuthenticationException cusException = (CustomAuthenticationException) message;
            authResponse.setCode(cusException.getCode());
            authResponse.setMsg(cusException.getMessage());
        } else {
            authResponse.setCode(AUTH_ERROR_CODE);
            authResponse.setMsg(message.getMessage());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        response.getOutputStream().write(objectMapper.writeValueAsBytes(authResponse));
    }

    @Data
    static class AuthResponse implements Serializable {
        private Integer code;
        private String msg;
        private Object result;
    }
}
