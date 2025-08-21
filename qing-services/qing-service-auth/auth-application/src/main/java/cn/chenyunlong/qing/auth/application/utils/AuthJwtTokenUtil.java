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

package cn.chenyunlong.qing.auth.application.utils;

import cn.chenyunlong.qing.auth.domain.user.QingUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT令牌工具类
 *
 * @author 陈云龙
 */
@Slf4j
@Component
public class AuthJwtTokenUtil {

    @Setter
    @Getter
    @Value("${jwt.secret:qingSecretKey12345678901234567890}")
    private String secret;

    @Setter
    @Getter
    @Value("${jwt.expiration:86400}")
    private Long expiration;

    @Value("${jwt.refreshExpiration:604800}")
    private Long refreshExpiration;

    /**
     * 从令牌中获取用户ID
     *
     * @param token 令牌
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        String userIdStr = getClaimFromToken(token, Claims::getSubject);
        return Long.parseLong(userIdStr);
    }

    /**
     * 从令牌中获取过期时间
     *
     * @param token 令牌
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 从令牌中获取声明
     *
     * @param token          令牌
     * @param claimsResolver 声明解析器
     * @param <T>            声明类型
     * @return 声明
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 从令牌中获取所有声明
     *
     * @param token 令牌
     * @return 所有声明
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
            .setSigningKey(getSigningKey())
            .parseClaimsJws(token)
            .getBody();
    }

    /**
     * 检查令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            log.error("Token expired check failed", e);
            return true;
        }
    }

    /**
     * 生成令牌
     *
     * @param userId 用户ID
     * @return 令牌
     */
    public String generateToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userId.toString(), expiration * 1000);
    }

    /**
     * 生成刷新令牌
     *
     * @param userId 用户ID
     * @return 刷新令牌
     */
    public String generateRefreshToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("refresh", true);
        return doGenerateToken(claims, userId.toString(), refreshExpiration * 1000);
    }

    /**
     * 生成令牌
     *
     * @param claims           声明
     * @param subject          主题
     * @param expirationMillis 过期时间（毫秒）
     * @return 令牌
     */
    private String doGenerateToken(Map<String, Object> claims, String subject, long expirationMillis) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expirationMillis);

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(createdDate)
            .setExpiration(expirationDate)
            .signWith(SignatureAlgorithm.HS512, getSigningKey())
            .compact();
    }

    /**
     * 验证令牌
     *
     * @param token 令牌
     * @return 是否有效
     */
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            log.error("Token validation failed", e);
            return false;
        }
    }

    public boolean validateToken(String jwtToken, QingUser user) {
        try {
            return !isTokenExpired(jwtToken);
        } catch (Exception e) {
            log.error("Token validation failed", e);
            return false;
        }
    }

    /**
     * 获取签名密钥
     *
     * @return 签名密钥
     */
    private String getSigningKey() {
        return secret;
    }
}
