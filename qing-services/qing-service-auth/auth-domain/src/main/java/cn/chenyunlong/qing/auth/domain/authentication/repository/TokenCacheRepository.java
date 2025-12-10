package cn.chenyunlong.qing.auth.domain.authentication.repository;

import java.util.Map;

/**
 * 令牌缓存能力接口（端口）。
 * 将黑名单、刷新令牌存储、用户令牌记录等能力从应用层解耦，
 * 由基础设施层提供具体实现（如 Redis）。
 */
public interface TokenCacheRepository {

    /**
     * 判断刷新令牌是否存在。
     */
    boolean existsRefreshToken(String tokenId);

    /**
     * 将令牌加入黑名单。
     *
     * @param tokenId   令牌唯一标识（如 jti）
     * @param ttlMillis 过期毫秒数
     */
    void addBlacklist(String tokenId, long ttlMillis);

    /**
     * 判断令牌是否在黑名单。
     */
    boolean isBlacklisted(String tokenId);

    /**
     * 列出用户下的所有令牌记录（tokenId -> tokenInfo）。
     */
    Map<String, String> listUserTokens(Long userId);

    /**
     * 删除刷新令牌记录。
     */
    void removeRefreshToken(String tokenId);

    /**
     * 删除用户的令牌记录哈希。
     */
    void removeAllUserTokens(Long userId);

    /**
     * 记录用户令牌信息，并设置哈希的过期时间（秒）。
     */
    void putUserToken(Long userId, String tokenId, String tokenInfo, long ttlSeconds);

    /**
     * 删除指定的用户令牌条目。
     */
    void removeUserToken(Long userId, String tokenId);

    /**
     * 存储刷新令牌。
     *
     * @param tokenId   刷新令牌ID
     * @param value     刷新令牌内容（可为序列化后的 JSON）
     * @param ttlMillis 过期毫秒数
     */
    void storeRefreshToken(String tokenId, String value, long ttlMillis);
}
