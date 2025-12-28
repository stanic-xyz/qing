/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions username the Mulan PSL v2.
 * You may obtain a copy username Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.chenyunlong.qing.auth.application.service;

import cn.chenyunlong.qing.auth.domain.authentication.repository.TokenCacheRepository;
import cn.chenyunlong.qing.auth.domain.user.User;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT令牌服务
 *
 * <p>
 * 提供JWT令牌的完整生命周期管理：
 * </p>
 * <ul>
 * <li>令牌生成：生成访问令牌和刷新令牌</li>
 * <li>令牌验证：验证令牌的有效性和完整性</li>
 * <li>令牌刷新：基于刷新令牌生成新的访问令牌</li>
 * <li>令牌撤销：将令牌加入黑名单</li>
 * <li>黑名单管理：管理已撤销的令牌</li>
 * </ul>
 *
 * <p>
 * 安全特性：
 * </p>
 * <ul>
 * <li>使用HMAC-SHA256算法签名</li>
 * <li>支持令牌黑名单机制</li>
 * <li>支持令牌自动刷新</li>
 * <li>支持多设备登录管理</li>
 * </ul>
 *
 * @author 陈云龙
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final TokenCacheRepository tokenCacheRepository;

    /**
     * JWT 密钥
     */
    @Value("${qing.jwt.secret:qing-auth-service-jwt-secret-key-2025}")
    private String jwtSecret;

    /**
     * 访问令牌过期时间（秒）
     */
    @Value("${qing.jwt.access-token-expiration:3600}")
    private Long accessTokenExpiration;

    /**
     * 刷新令牌过期时间（秒）
     */
    @Value("${qing.jwt.refresh-token-expiration:604800}")
    private Long refreshTokenExpiration;

    /**
     * 令牌发行者
     */
    @Value("${qing.jwt.issuer:qing-auth-service}")
    private String issuer;

    /**
     * Redis键前缀
     */
    private static final String BLACKLIST_PREFIX = "jwt:blacklist:";
    private static final String REFRESH_TOKEN_PREFIX = "jwt:refresh:";
    private static final String USER_TOKENS_PREFIX = "jwt:user:";

    /**
     * 生成访问令牌
     *
     * @param user 用户信息
     * @return 访问令牌
     */
    public String generateAccessToken(User user) {
        return generateAccessToken(user, null);
    }

    /**
     * 生成访问令牌
     *
     * @param user     用户信息
     * @param deviceId 设备ID
     * @return 访问令牌
     */
    public String generateAccessToken(User user, String deviceId) {
        Date now = new Date();

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId().id());
        claims.put("username", user.getUsername().value());
        claims.put("type", "access");
        claims.put("deviceId", deviceId);
        // JWT ID，用于唯一标识
        claims.put("jti", IdUtil.fastSimpleUUID());

        Date expiration = new Date(now.getTime() + accessTokenExpiration * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername().value())
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 生成访问令牌（包含角色与权限）
     *
     * @param user        用户信息
     * @param deviceId    设备ID
     * @param roles       角色列表（编码或ID）
     * @param permissions 权限列表（编码）
     * @return 访问令牌
     */
    public String generateAccessToken(User user, String deviceId, java.util.List<String> roles,
                                      java.util.List<String> permissions) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId().id());
        claims.put("username", user.getUsername().value());
        claims.put("type", "access");
        claims.put("deviceId", deviceId);
        claims.put("jti", IdUtil.fastSimpleUUID());
        claims.put("roles", roles);
        claims.put("permissions", permissions);

        Date date = new Date();
        Date expiration = DateUtil.offsetMillisecond(date, (int) (accessTokenExpiration * 1000));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername().value())
                .setIssuer(issuer)
                .setIssuedAt(date)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 生成刷新令牌
     *
     * @param user 用户信息
     * @return 刷新令牌
     */
    public String generateRefreshToken(User user) {
        return generateRefreshToken(user, null);
    }

    /**
     * 生成刷新令牌
     *
     * @param user     用户信息
     * @param deviceId 设备ID
     * @return 刷新令牌
     */
    public String generateRefreshToken(User user, String deviceId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + refreshTokenExpiration * 1000);

        String refreshTokenId = IdUtil.fastSimpleUUID();

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId().id());
        claims.put("username", user.getUsername().value());
        claims.put("type", "refresh");
        claims.put("deviceId", deviceId);
        claims.put("jti", refreshTokenId);

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername().value())
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        // 将刷新令牌存储到Redis
        storeRefreshToken(refreshTokenId, user.getId(), deviceId, refreshTokenExpiration * 1000);

        // 记录用户的令牌
        recordUserToken(user.getId(), refreshTokenId, deviceId);

        return refreshToken;
    }

    /**
     * 验证令牌
     *
     * @param token 令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            if (StrUtil.isBlank(token)) {
                return false;
            }

            // 检查黑名单
            if (isTokenBlacklisted(token)) {
                log.warn("令牌已在黑名单中: {}", maskToken(token));
                return false;
            }

            // 解析令牌
            Claims claims = parseToken(token);
            if (claims == null) {
                return false;
            }

            // 检查过期时间
            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                log.debug("令牌已过期: {}", maskToken(token));
                return false;
            }
            return true;
        } catch (Exception e) {
            log.warn("令牌验证失败: {}, 错误: {}", maskToken(token), e.getMessage());
            return false;
        }
    }

    /**
     * 解析令牌
     *
     * @param token 令牌
     * @return 令牌声明
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException exception) {
            log.debug("令牌已过期: {}", maskToken(token));
            return null;
        } catch (UnsupportedJwtException e) {
            log.warn("不支持的令牌格式: {}", maskToken(token));
            return null;
        } catch (MalformedJwtException e) {
            log.warn("令牌格式错误: {}", maskToken(token));
            return null;
        } catch (SecurityException e) {
            log.warn("令牌签名验证失败: {}", maskToken(token));
            return null;
        } catch (IllegalArgumentException e) {
            log.warn("令牌参数错误: {}", maskToken(token));
            return null;
        }
    }

    /**
     * 从令牌中获取用户ID
     *
     * @param token 令牌
     * @return 用户ID
     */
    public UserId getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims != null) {
            Long userId = claims.get("userId", Long.class);
            return userId != null ? UserId.of(userId) : null;
        }
        return null;
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims != null ? claims.getSubject() : null;
    }

    /**
     * 从令牌中获取设备ID
     *
     * @param token 令牌
     * @return 设备ID
     */
    public String getDeviceIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims != null ? claims.get("deviceId", String.class) : null;
    }

    /**
     * 刷新访问令牌
     *
     * @param refreshToken 刷新令牌
     * @param user         用户信息
     * @return 新的访问令牌
     */
    public String refreshAccessToken(String refreshToken, User user) {
        // 验证刷新令牌
        if (!validateRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("无效的刷新令牌");
        }

        Claims claims = parseToken(refreshToken);
        if (claims == null) {
            throw new IllegalArgumentException("无法解析刷新令牌");
        }

        String deviceId = claims.get("deviceId", String.class);

        // 生成新的访问令牌
        return generateAccessToken(user, deviceId);
    }

    /**
     * 验证刷新令牌
     *
     * @param refreshToken 刷新令牌
     * @return 是否有效
     */
    public boolean validateRefreshToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            return false;
        }

        Claims claims = parseToken(refreshToken);
        if (claims == null) {
            return false;
        }

        // 检查令牌类型
        String type = claims.get("type", String.class);
        if (!"refresh".equals(type)) {
            return false;
        }

        // 检查Redis中是否存在
        String jti = claims.get("jti", String.class);
        return tokenCacheRepository.existsRefreshToken(jti);
    }

    /**
     * 撤销令牌（加入黑名单）
     *
     * @param token 令牌
     */
    public void revokeToken(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return;
        }

        String jti = claims.get("jti", String.class);
        if (StrUtil.isNotBlank(jti)) {
            Date expiration = claims.getExpiration();
            long ttl = expiration.getTime() - System.currentTimeMillis();

            if (ttl > 0) {
                tokenCacheRepository.addBlacklist(jti, ttl);
                log.info("令牌已撤销: jti={}", jti);
            }
        }
    }

    /**
     * 撤销用户的所有令牌
     *
     * @param userId 用户ID
     */
    public void revokeAllUserTokens(UserId userId) {
        Map<String, String> userTokens = tokenCacheRepository.listUserTokens(userId.id());
        for (String tokenId : userTokens.keySet()) {
            tokenCacheRepository.addBlacklist(tokenId, refreshTokenExpiration * 1000);
            tokenCacheRepository.removeRefreshToken(tokenId);
        }
        tokenCacheRepository.removeAllUserTokens(userId.id());

        log.info("用户所有令牌已撤销: user={}", userId.id());
    }

    /**
     * 撤销用户指定设备的令牌
     *
     * @param userId   用户ID
     * @param deviceId 设备ID
     */
    public void revokeUserDeviceTokens(UserId userId, String deviceId) {
        Map<String, String> userTokens = tokenCacheRepository.listUserTokens(userId.id());
        for (Map.Entry<String, String> entry : userTokens.entrySet()) {
            String tokenInfo = entry.getValue();
            if (tokenInfo != null && tokenInfo.contains(deviceId)) {
                String tokenId = entry.getKey();
                tokenCacheRepository.addBlacklist(tokenId, refreshTokenExpiration * 1000);
                tokenCacheRepository.removeRefreshToken(tokenId);
                tokenCacheRepository.removeUserToken(userId.id(), tokenId);
            }
        }

        log.info("用户设备令牌已撤销: user={}, deviceId={}", userId.id(), deviceId);
    }

    /**
     * 检查令牌是否在黑名单中
     *
     * @param token 令牌
     * @return 是否在黑名单中
     */
    private boolean isTokenBlacklisted(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return false;
        }

        String jti = claims.get("jti", String.class);
        return StrUtil.isNotBlank(jti) && tokenCacheRepository.isBlacklisted(jti);
    }

    /**
     * 存储刷新令牌
     *
     * @param tokenId          令牌ID
     * @param userId           用户ID
     * @param deviceId         设备ID
     * @param expirationMillis 过期时间
     */
    private void storeRefreshToken(String tokenId, UserId userId, String deviceId, long expirationMillis) {
        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("userId", userId.id());
        tokenInfo.put("deviceId", deviceId);
        tokenInfo.put("createdAt", Instant.now());
        tokenInfo.put("expiration", Instant.ofEpochMilli(expirationMillis));

        tokenCacheRepository.storeRefreshToken(tokenId, JSONUtil.toJsonStr(tokenInfo), expirationMillis);
    }

    /**
     * 记录用户令牌
     *
     * @param userId   用户ID
     * @param tokenId  令牌ID
     * @param deviceId 设备ID
     */
    private void recordUserToken(UserId userId, String tokenId, String deviceId) {
        String tokenInfo = String.format("deviceId:%s,createdAt:%s",
                deviceId, DateUtil.formatDateTime(new Date()));
        tokenCacheRepository.putUserToken(userId.id(), tokenId, tokenInfo, refreshTokenExpiration);
    }

    /**
     * 获取签名密钥
     *
     * @return 签名密钥
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 掩码令牌（用于日志记录）
     *
     * @param token 令牌
     * @return 掩码后的令牌
     */
    private String maskToken(String token) {
        if (StrUtil.isBlank(token) || token.length() < 20) {
            return "***";
        }
        return token.substring(0, 10) + "***" + token.substring(token.length() - 10);
    }

    /**
     * 令牌信息
     */
    @Getter
    public static class TokenInfo {
        private final String accessToken;
        private final String refreshToken;
        private final Long expiresIn;
        private final String tokenType;

        public TokenInfo(String accessToken, String refreshToken, Long expiresIn) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.expiresIn = expiresIn;
            this.tokenType = "Bearer";
        }

    }

    /**
     * 生成完整的令牌信息
     *
     * @param user     用户信息
     * @param deviceId 设备ID
     * @return 令牌信息
     */
    public TokenInfo generateTokenInfo(User user, String deviceId) {
        String accessToken = generateAccessToken(user, deviceId);
        String refreshToken = generateRefreshToken(user, deviceId);
        return new TokenInfo(accessToken, refreshToken, accessTokenExpiration);
    }
}
