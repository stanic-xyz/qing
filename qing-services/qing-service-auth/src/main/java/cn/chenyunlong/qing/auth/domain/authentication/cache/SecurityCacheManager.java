package cn.chenyunlong.qing.auth.domain.authentication.cache;

import java.time.Duration;
import java.util.Optional;

/**
 * 安全策略缓存管理器接口
 * 抽象 Redis 缓存能力，支持内存实现
 */
public interface SecurityCacheManager {
    
    // ==================== IP 黑名单/白名单 ====================
    
    /**
     * 检查 IP 是否在黑名单中
     */
    boolean isIpBlacklisted(String ip);
    
    /**
     * 检查 IP 是否在白名单中
     */
    boolean isIpWhitelisted(String ip);
    
    /**
     * 添加 IP 到黑名单
     */
    void addIpToBlacklist(String ip);
    
    /**
     * 从黑名单移除 IP
     */
    void removeIpFromBlacklist(String ip);
    
    /**
     * 获取黑名单大小
     */
    long getBlacklistSize();
    
    // ==================== 登录失败计数 ====================
    
    /**
     * 记录登录失败
     * @param username 用户名
     * @return 当前失败次数
     */
    long recordLoginFailure(String username);
    
    /**
     * 重置登录失败计数
     */
    void resetLoginFailure(String username);
    
    /**
     * 获取登录失败次数
     */
    long getLoginFailures(String username);
    
    // ==================== Token 缓存 ====================
    
    /**
     * 检查刷新令牌是否存在
     */
    boolean existsRefreshToken(String tokenId);
    
    /**
     * 存储刷新令牌
     */
    void storeRefreshToken(String tokenId, String value, Duration ttl);
    
    /**
     * 删除刷新令牌
     */
    void deleteRefreshToken(String tokenId);
    
    // ==================== Token 黑名单 ====================
    
    /**
     * 添加 Token 到黑名单
     */
    void addToBlacklist(String tokenId, Duration ttl);
    
    /**
     * 检查 Token 是否在黑名单中
     */
    boolean isBlacklisted(String tokenId);
    
    // ==================== 用户 Token 管理 ====================
    
    /**
     * 存储用户的 Token
     */
    void storeUserToken(String userId, String tokenId, String tokenInfo, Duration ttl);
    
    /**
     * 获取用户的所有 Token 信息
     */
    String getUserToken(String userId, String tokenId);
    
    /**
     * 删除用户的指定 Token
     */
    void deleteUserToken(String userId, String tokenId);
    
    /**
     * 删除用户的所有 Token
     */
    void deleteAllUserTokens(String userId);
}
