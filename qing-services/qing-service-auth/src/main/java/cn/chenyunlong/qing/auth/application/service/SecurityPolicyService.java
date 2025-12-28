package cn.chenyunlong.qing.auth.application.service;

import cn.chenyunlong.qing.auth.domain.authentication.valueObject.IpAddress;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.PasswordExpiredHandlePolicy;
import cn.chenyunlong.qing.auth.domain.user.port.SecurityPolicyPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * 安全策略服务实现
 * 基于 Redis 实现动态 IP 名单和登录失败计数
 *
 * @author 陈云龙
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityPolicyService implements SecurityPolicyPort {

    private final StringRedisTemplate redisTemplate;

    private static final Integer DEFAULT_MAX_LOGIN_ATTEMPTS = 5;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(30);

    // Redis Keys
    private static final String IP_BLACKLIST_KEY = "auth:security:ip:blacklist";
    private static final String IP_WHITELIST_KEY = "auth:security:ip:whitelist";
    private static final String LOGIN_FAIL_PREFIX = "auth:security:login:fail:";

    @Override
    public PasswordExpiredHandlePolicy getPasswordExpiredPolicy() {
        // 默认策略：密码过期强制修改
        return PasswordExpiredHandlePolicy.FAIL;
    }

    @Override
    public int getMaxLoginAttempts() {
        return DEFAULT_MAX_LOGIN_ATTEMPTS;
    }

    @Override
    public boolean isIpAllowed(IpAddress ipAddress) {
        String ip = ipAddress.getValue();

        // 1. 如果白名单不为空，则必须在白名单中
        Long whitelistSize = redisTemplate.opsForSet().size(IP_WHITELIST_KEY);
        if (whitelistSize != null && whitelistSize > 0) {
            Boolean isWhitelisted = redisTemplate.opsForSet().isMember(IP_WHITELIST_KEY, ip);
            return Boolean.TRUE.equals(isWhitelisted);
        }

        // 2. 检查黑名单
        Boolean isBlacklisted = redisTemplate.opsForSet().isMember(IP_BLACKLIST_KEY, ip);
        return !Boolean.TRUE.equals(isBlacklisted);
    }

    public boolean isLoginTimeAllowed() {
        // 简单实现：全天允许
        return true;
    }

    public boolean isDeviceAllowed(String userAgent) {
        // 简单实现：允许所有设备
        return true;
    }

    /**
     * 记录登录失败
     *
     * @param username 用户名
     * @return 当前失败次数
     */
    public long recordLoginFailure(String username) {
        String key = LOGIN_FAIL_PREFIX + username;
        Long attempts = redisTemplate.opsForValue().increment(key);
        if (attempts != null && attempts == 1) {
            redisTemplate.expire(key, LOCK_DURATION);
        }
        return attempts != null ? attempts : 0;
    }

    /**
     * 重置登录失败计数
     *
     * @param username 用户名
     */
    public void resetLoginFailure(String username) {
        String key = LOGIN_FAIL_PREFIX + username;
        redisTemplate.delete(key);
    }

    /**
     * 获取当前登录失败次数
     *
     * @param username 用户名
     * @return 失败次数
     */
    public long getLoginFailures(String username) {
        String key = LOGIN_FAIL_PREFIX + username;
        String val = redisTemplate.opsForValue().get(key);
        return val != null ? Long.parseLong(val) : 0;
    }

    /**
     * 添加 IP 到黑名单
     */
    public void addIpToBlacklist(String ip) {
        redisTemplate.opsForSet().add(IP_BLACKLIST_KEY, ip);
    }

    /**
     * 从黑名单移除 IP
     */
    public void removeIpFromBlacklist(String ip) {
        redisTemplate.opsForSet().remove(IP_BLACKLIST_KEY, ip);
    }
}
