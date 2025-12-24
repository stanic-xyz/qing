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
import cn.chenyunlong.qing.security.config.utils.JwtTokenUtil;
import cn.hutool.core.collection.CollUtil;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Token操作方法。
 *
 * @author 陈云龙
 * @since 2020-09-13
 */
@Slf4j
@Component
@AllArgsConstructor
public class TokenProvider {

    private static final String AUTHORITIES_HEADER = "auth";
    private static final String FIELD_USER_ID = "userId";
    private static final String USER_INFO = "userInfo";
    private final SecurityProperties securityProperties;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * 从jwt中获取token信息。
     *
     * @param jwtToken jwt认证信息
     */
    public Authentication getAuthentication(String jwtToken) {
        Claims claims = jwtTokenUtil.getClaimsFromToken(jwtToken);
        Collection<? extends GrantedAuthority> authorities;
        Object object = claims.get(AUTHORITIES_HEADER);
        if (object != null) {
            authorities = Arrays
                .stream(object.toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        } else {
            authorities = CollUtil.newArrayList();
        }
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
    }

    /**
     * 验证认证信息是否有效。
     *
     * @param jwtToken jwtToken
     * @return json是否合法
     */
    public boolean validateToken(String jwtToken) {
        return !jwtTokenUtil.isTokenExpired(jwtToken);
    }
}
