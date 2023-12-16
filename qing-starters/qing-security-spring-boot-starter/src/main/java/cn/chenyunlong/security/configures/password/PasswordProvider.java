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

package cn.chenyunlong.security.configures.password;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Slf4j
public class PasswordProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {
        Assert.isInstanceOf(PasswordToken.class, authentication,
            () -> "Only AuthingToken is supported");
        PasswordToken passwordToken = (PasswordToken) authentication;
        log.info("用户密码登录逻辑开始");
        log.info("username:{},password:{}", passwordToken.getUsername(),
            passwordToken.getPassword());
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (PasswordToken.class.isAssignableFrom(authentication));
    }
}
