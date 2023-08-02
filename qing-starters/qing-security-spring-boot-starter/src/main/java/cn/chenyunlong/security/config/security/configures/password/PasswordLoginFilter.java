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

package cn.chenyunlong.security.config.security.configures.password;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class PasswordLoginFilter extends AbstractAuthenticationProcessingFilter {

    public PasswordLoginFilter(AuthenticationManager authenticationManager) {
        super("/auth/passLogin", authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
        throws AuthenticationException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        PasswordLoginRequest loginRequest =
            mapper.readValue(request.getReader(), PasswordLoginRequest.class);
        PasswordToken passToken = new PasswordToken(Collections.emptyList());
        passToken.setUsername(loginRequest.getUsername());
        passToken.setPassword(loginRequest.getPassword());
        return this.getAuthenticationManager().authenticate(passToken);
    }
}
