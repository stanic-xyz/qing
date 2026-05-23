package cn.chenyunlong.qing.auth.infrastructure.cache;

import cn.chenyunlong.qing.auth.domain.authentication.cache.SecurityCacheManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存版本的 SecurityCacheManager 实现
 * 用于测试环境
 */
@Component
@Profile("test")
public class InMemorySecurityCacheManager implements SecurityCacheManager {
    
    private final Set<String> ipBlacklist = ConcurrentHashMap.newKeySet();
    private final Set<String> ipWhitelist = ConcurrentHashMap.newKeySet();
    private final Map<String, FailureEntry> loginFailures = new ConcurrentHashMap<>();
    private final Set<String> tokenBlacklist = ConcurrentHashMap.newKeySet();
    private final Map<String, String> refreshTokens = new ConcurrentHashMap<>();
    private final Map<String, TokenEntry> userTokens = new ConcurrentHashMap<>();
    
    @Override
    public boolean isIpBlacklisted(String ip) {
        return ipBlacklist.contains(ip);
    }
    
    @Override
    public boolean isIpWhitelisted(String ip) {
        return ipWhitelist.contains(ip);
    }
    
    @Override
    public void addIpToBlacklist(String ip) {
        ipBlacklist.add(ip);
    }
    
    @Override
    public void removeIpFromBlacklist(String ip) {
        ipBlacklist.remove(ip);
    }
    
    @Override
    public long getBlacklistSize() {
        cleanupExpiredEntries();
        return ipBlacklist.size();
    }
    
    @Override
    public long recordLoginFailure(String username) {
        cleanupExpiredEntries();
        FailureEntry entry = loginFailures.computeIfAbsent(username, k -> new FailureEntry());
        entry.increment();
        return entry.getCount();
    }
    
    @Override
    public void resetLoginFailure(String username) {
        loginFailures.remove(username);
    }
    
    @Override
    public long getLoginFailures(String username) {
        cleanupExpiredEntries();
        FailureEntry entry = loginFailures.get(username);
        return entry != null ? entry.getCount() : 0;
    }
    
    @Override
    public boolean existsRefreshToken(String tokenId) {
        cleanupExpiredEntries();
        return refreshTokens.containsKey(tokenId);
    }
    
    @Override
    public void storeRefreshToken(String tokenId, String value, Duration ttl) {
        refreshTokens.put(tokenId, value);
    }
    
    @Override
    public void deleteRefreshToken(String tokenId) {
        refreshTokens.remove(tokenId);
    }
    
    @Override
    public void addToBlacklist(String tokenId, Duration ttl) {
        tokenBlacklist.add(tokenId);
    }
    
    @Override
    public boolean isBlacklisted(String tokenId) {
        return tokenBlacklist.contains(tokenId);
    }
    
    @Override
    public void storeUserToken(String userId, String tokenId, String tokenInfo, Duration ttl) {
        String key = userId + ":" + tokenId;
        userTokens.put(key, new TokenEntry(tokenInfo));
    }
    
    @Override
    public String getUserToken(String userId, String tokenId) {
        String key = userId + ":" + tokenId;
        TokenEntry entry = userTokens.get(key);
        if (entry == null || entry.isExpired()) {
            userTokens.remove(key);
            return null;
        }
        return entry.getValue();
    }
    
    @Override
    public void deleteUserToken(String userId, String tokenId) {
        String key = userId + ":" + tokenId;
        userTokens.remove(key);
    }
    
    @Override
    public void deleteAllUserTokens(String userId) {
        userTokens.keySet().removeIf(key -> key.startsWith(userId + ":"));
    }
    
    private void cleanupExpiredEntries() {
        loginFailures.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
    
    private static class FailureEntry {
        private final long createdAt = System.currentTimeMillis();
        private long count = 0;
        
        public void increment() {
            count++;
        }
        
        public long getCount() {
            return count;
        }
        
        public boolean isExpired() {
            return System.currentTimeMillis() - createdAt > Duration.ofMinutes(30).toMillis();
        }
    }
    
    private static class TokenEntry {
        private final String value;
        private final long createdAt = System.currentTimeMillis();
        
        public TokenEntry(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public boolean isExpired() {
            return false; // 简化实现，不处理过期
        }
    }
}
