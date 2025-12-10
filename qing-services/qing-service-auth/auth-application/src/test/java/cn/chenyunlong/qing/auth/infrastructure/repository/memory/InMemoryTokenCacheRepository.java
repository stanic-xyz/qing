package cn.chenyunlong.qing.auth.infrastructure.repository.memory;

import cn.chenyunlong.qing.auth.domain.authentication.repository.TokenCacheRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 内存 Token 缓存实现（测试用）
 */
@Repository
public class InMemoryTokenCacheRepository implements TokenCacheRepository {
    private final Set<String> blacklist = new HashSet<>();
    private final Map<Long, Map<String, String>> userTokens = new HashMap<>();
    private final Map<String, String> refreshTokens = new HashMap<>();

    /**
     * 是否存在刷新令牌
     */
    @Override
    public boolean existsRefreshToken(String tokenId) {return refreshTokens.containsKey(tokenId);}

    /**
     * 加入黑名单
     */
    @Override
    public void addBlacklist(String tokenId, long ttlMillis) {blacklist.add(tokenId);}

    /**
     * 是否黑名单
     */
    @Override
    public boolean isBlacklisted(String tokenId) {return blacklist.contains(tokenId);}

    /**
     * 列出用户令牌
     */
    @Override
    public Map<String, String> listUserTokens(Long userId) {return userTokens.getOrDefault(userId, Map.of());}

    /**
     * 删除刷新令牌
     */
    @Override
    public void removeRefreshToken(String tokenId) {refreshTokens.remove(tokenId);}

    /**
     * 删除用户全部令牌
     */
    @Override
    public void removeAllUserTokens(Long userId) {userTokens.remove(userId);}

    /**
     * 写入用户令牌
     */
    @Override
    public void putUserToken(Long userId, String tokenId, String tokenInfo, long ttlSeconds) {
        userTokens.computeIfAbsent(userId, k -> new HashMap<>()).put(tokenId, tokenInfo);
    }

    /**
     * 删除用户令牌
     */
    @Override
    public void removeUserToken(Long userId, String tokenId) {
        Map<String, String> map = userTokens.get(userId);
        if (map != null) {
            map.remove(tokenId);
        }
    }

    /**
     * 存储刷新令牌
     */
    @Override
    public void storeRefreshToken(String tokenId, String value, long ttlMillis) {refreshTokens.put(tokenId, value);}
}
