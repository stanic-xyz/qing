package cn.chenyunlong.qing.auth.interfaces.cache;

import cn.chenyunlong.qing.auth.domain.authentication.repository.TokenCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 基于 Redis 的令牌缓存实现。
 */
@Component
@RequiredArgsConstructor
public class RedisTokenCacheRepository implements TokenCacheRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String BLACKLIST_PREFIX = "jwt:blacklist:";
    private static final String REFRESH_TOKEN_PREFIX = "jwt:refresh:";
    private static final String USER_TOKENS_PREFIX = "jwt:user_tokens:";

    @Override
    public boolean existsRefreshToken(String tokenId) {
        return redisTemplate.hasKey(REFRESH_TOKEN_PREFIX + tokenId);
    }

    @Override
    public void addBlacklist(String tokenId, long ttlMillis) {
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + tokenId, "revoked", ttlMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isBlacklisted(String tokenId) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX + tokenId);
    }

    @Override
    public Map<String, String> listUserTokens(Long userId) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(USER_TOKENS_PREFIX + userId);
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            String key = String.valueOf(entry.getKey());
            String val = entry.getValue() == null ? null : String.valueOf(entry.getValue());
            result.put(key, val);
        }
        return result;
    }

    @Override
    public void removeRefreshToken(String tokenId) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + tokenId);
    }

    @Override
    public void removeAllUserTokens(Long userId) {
        redisTemplate.delete(USER_TOKENS_PREFIX + userId);
    }

    @Override
    public void putUserToken(Long userId, String tokenId, String tokenInfo, long ttlSeconds) {
        String key = USER_TOKENS_PREFIX + userId;
        redisTemplate.opsForHash().put(key, tokenId, tokenInfo);
        redisTemplate.expire(key, ttlSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void removeUserToken(Long userId, String tokenId) {
        redisTemplate.opsForHash().delete(USER_TOKENS_PREFIX + userId, tokenId);
    }

    @Override
    public void storeRefreshToken(String tokenId, String value, long ttlMillis) {
        redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + tokenId, value, ttlMillis, TimeUnit.MILLISECONDS);
    }
}
