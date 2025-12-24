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

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.qing.security.base.extension.UserContextAware;
import cn.chenyunlong.qing.security.exception.CustomAuthenticationException;
import cn.chenyunlong.qing.security.exception.ParseTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class JwtAuthenticationProvider extends BaseAuthenticationProvider
    implements AuthenticationProvider {

    private final UserContextAware userContextAware;

    public JwtAuthenticationProvider(UserContextAware userContextAware) {
        this.userContextAware = userContextAware;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = authentication.getCredentials().toString();
        BaseJwtUser jwtUser;
        if (Objects.isNull(userContextAware)) {
            return authentication;
        } else {
            try {
                jwtUser = userContextAware.processToken(token);
            } catch (ParseTokenException e) {
                throw new CustomAuthenticationException(e.getCode(), e.getMsg());
            } catch (Exception e) {
                throw new CustomAuthenticationException(CodeEnum.SystemError.getValue(),
                    CodeEnum.SystemError.getName());
            }
            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(jwtUser, token, jwtUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
            return authToken;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthToken.class.equals(authentication);
    }
}
