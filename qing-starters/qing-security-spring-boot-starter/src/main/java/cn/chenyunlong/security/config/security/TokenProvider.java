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

import cn.chenyunlong.common.utils.JwtUtil;
import cn.chenyunlong.security.config.SecurityProperties;
import cn.chenyunlong.security.config.security.dto.UserInfoVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * @author Stan
 */
@Slf4j
@Component
@AllArgsConstructor
public class TokenProvider {

    private static final String AUTHORITIES_HEADER = "auth";
    private static final String FIELD_USER_ID = "userId";
    private static final String USER_INFO = "userInfo";
    private final SecurityProperties securityProperties;
    private final ObjectMapper objectMapper;


    /**
     * 通过token获取用户信息
     *
     * @param jwtToken token 信息
     * @return 用户信息
     */
    @SuppressWarnings("unused")
    public UserInfoVO getUserNameFromToken(String jwtToken) {
        try {
            Claims claims = JwtUtil.parseJWT(jwtToken, securityProperties.getSecretKey());
            return objectMapper.readValue(claims.getSubject(), UserInfoVO.class);
        } catch (SecurityException | MalformedJwtException e) {
            log.debug("Invalid JWT signature.");
        } catch (ExpiredJwtException e) {
            log.debug("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.debug("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.debug("JWT token compact of handler are invalid.");
        } catch (JsonProcessingException e) {
            log.debug("parse jsonObject failed.");
        } catch (Exception e) {
            log.error("解析jwt token 错误", e);
        }
        return null;
    }

    /**
     * 酱token生成jwt生成token信息
     *
     * @param authentication token
     * @param rememberMe     是否记住我，认证信息时间设置为一个月
     * @return jwtToken信息
     */
    public String createJwtToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();
        Date validity;
        //是否颁发一个长久的令牌
        if (rememberMe) {
            validity = new Date(now + securityProperties.getTokenValidityInMilliseconds());
        } else {
            validity =
                new Date(now + securityProperties.getTokenValidityInMillisecondsForRememberMe());
        }

        return Jwts
            .builder()
            .setSubject((String) authentication.getPrincipal())
            .claim(AUTHORITIES_HEADER, authorities)
            .claim(USER_INFO, authentication.getPrincipal())
            .signWith(SignatureAlgorithm.HS256, securityProperties.getSecretKey())
            .setExpiration(validity)
            .compact();
    }

    /**
     * 酱token生成jwt生成token信息
     *
     * @param userinfoVo token
     * @param rememberMe 是否记住我，认证信息时间设置为一个月
     * @return jwtToken信息
     */
    public String createJwtToken(UserInfoVO userinfoVo, boolean rememberMe) {
        long now = System.currentTimeMillis();
        Date validity;
        //是否颁发一个长久的令牌
        if (rememberMe) {
            validity = new Date(now + securityProperties.getTokenValidityInMilliseconds());
        } else {
            validity =
                new Date(now + securityProperties.getTokenValidityInMillisecondsForRememberMe());
        }

        try {
            return Jwts
                .builder()
                .setSubject(objectMapper.writeValueAsString(userinfoVo))
                .claim(AUTHORITIES_HEADER, "authorities")
                .claim(FIELD_USER_ID, "")
                .claim("userAgent", "")
                .signWith(SignatureAlgorithm.HS512, securityProperties.getSecretKey())
                .setExpiration(validity)
                .compact();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从jwt中获取token信息
     *
     * @param jwt jwt认证信息
     */
    public Authentication getAuthentication(String jwt) {
        Claims claims =
            Jwts.parser().setSigningKey(securityProperties.getSecretKey()).parseClaimsJws(jwt)
                .getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays
            .stream(claims.get(AUTHORITIES_HEADER).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
    }

    /**
     * 验证认证信息是否有效
     *
     * @param jwtToken jwtToken
     * @return json是否合法
     */
    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parser().setSigningKey(securityProperties.getSecretKey()).parseClaimsJws(jwtToken);
            return true;
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException |
                 ExpiredJwtException |
                 IllegalArgumentException ignore) {
            return false;
        }
    }
}
