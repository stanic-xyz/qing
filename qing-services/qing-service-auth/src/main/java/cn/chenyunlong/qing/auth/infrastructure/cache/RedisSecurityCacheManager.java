package cn.chenyunlong.qing.auth.infrastructure.cache;

import cn.chenyunlong.qing.auth.domain.authentication.cache.SecurityCacheManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 基于 Redis 的 SecurityCacheManager 实现
 * 生产环境使用
 */
@Component
@RequiredArgsConstructor
@Profile("!test")
public class RedisSecurityCacheManager implements SecurityCacheManager {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String IP_BLACKLIST_KEY = "auth:security:ip:blacklist";
    private static final String IP_WHITELIST_KEY = "auth:security:ip:whitelist";
    private static final String LOGIN_FAIL_PREFIX = "auth:security:login:fail:";
    private static final Duration FAILURE_LOCK_DURATION = Duration.ofMinutes(30);

    @Override
    public boolean isIpBlacklisted(String ip) {
        SetOperations<String, Object> setOps = redisTemplate.opsForSet();
        Boolean isMember = setOps.isMember(IP_BLACKLIST_KEY, ip);
        return Boolean.TRUE.equals(isMember);
    }

    @Override
    public boolean isIpWhitelisted(String ip) {
        SetOperations<String, Object> setOps = redisTemplate.opsForSet();
        Long size = setOps.size(IP_WHITELIST_KEY);
        if (size != null && size > 0) {
            Boolean isMember = setOps.isMember(IP_WHITELIST_KEY, ip);
            return Boolean.TRUE.equals(isMember);
        }
        return false;
    }

    @Override
    public void addIpToBlacklist(String ip) {
        redisTemplate.opsForSet().add(IP_BLACKLIST_KEY, ip);
    }

    @Override
    public void removeIpFromBlacklist(String ip) {
        redisTemplate.opsForSet().remove(IP_BLACKLIST_KEY, ip);
    }

    @Override
    public long getBlacklistSize() {
        Long size = redisTemplate.opsForSet().size(IP_BLACKLIST_KEY);
        return size != null ? size : 0;
    }

    @Override
    public long recordLoginFailure(String username) {
        String key = LOGIN_FAIL_PREFIX + username;
        Long attempts = redisTemplate.opsForValue().increment(key);
        if (attempts != null && attempts == 1) {
            redisTemplate.expire(key, FAILURE_LOCK_DURATION);
        }
        return attempts != null ? attempts : 0;
    }

    @Override
    public void resetLoginFailure(String username) {
        String key = LOGIN_FAIL_PREFIX + username;
        redisTemplate.delete(key);
    }

    @Override
    public long getLoginFailures(String username) {
        String key = LOGIN_FAIL_PREFIX + username;
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return 0;
        }
        return Long.parseLong(value.toString());
    }

    @Override
    public boolean existsRefreshToken(String tokenId) {
        String key = "jwt:refresh:" + tokenId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public void storeRefreshToken(String tokenId, String value, Duration ttl) {
        String key = "jwt:refresh:" + tokenId;
        redisTemplate.opsForValue().set(key, value, ttl.toMillis(), java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    @Override
    public void deleteRefreshToken(String tokenId) {
        String key = "jwt:refresh:" + tokenId;
        redisTemplate.delete(key);
    }

    @Override
    public void addToBlacklist(String tokenId, Duration ttl) {
        String key = "jwt:blacklist:" + tokenId;
        redisTemplate.opsForValue().set(key, "revoked", ttl.toMillis(), java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isBlacklisted(String tokenId) {
        String key = "jwt:blacklist:" + tokenId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public void storeUserToken(String userId, String tokenId, String tokenInfo, Duration ttl) {
        String key = "jwt:user_tokens:" + userId;
        redisTemplate.opsForHash().put(key, tokenId, tokenInfo);
        redisTemplate.expire(key, ttl.toSeconds(), java.util.concurrent.TimeUnit.SECONDS);
    }

    @Override
    public String getUserToken(String userId, String tokenId) {
        String key = "jwt:user_tokens:" + userId;
        Object value = redisTemplate.opsForHash().get(key, tokenId);
        return value != null ? value.toString() : null;
    }

    @Override
    public void deleteUserToken(String userId, String tokenId) {
        String key = "jwt:user_tokens:" + userId;
        redisTemplate.opsForHash().delete(key, tokenId);
    }

    @Override
    public void deleteAllUserTokens(String userId) {
        String key = "jwt:user_tokens:" + userId;
        redisTemplate.delete(key);
    }
}
