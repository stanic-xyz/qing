package cn.chenyunlong.qing.auth.application.service;

import cn.chenyunlong.qing.auth.domain.authentication.cache.SecurityCacheManager;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.IpAddress;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.PasswordExpiredHandlePolicy;
import cn.chenyunlong.qing.auth.domain.user.port.SecurityPolicyPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * 安全策略服务实现
 * 基于缓存实现动态 IP 名单和登录失败计数
 *
 * @author 陈云龙
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityPolicyService implements SecurityPolicyPort {

    private final SecurityCacheManager securityCacheManager;

    private static final int DEFAULT_MAX_LOGIN_ATTEMPTS = 5;

    @Override
    public PasswordExpiredHandlePolicy getPasswordExpiredPolicy() {
        return PasswordExpiredHandlePolicy.FAIL;
    }

    @Override
    public int getMaxLoginAttempts() {
        return DEFAULT_MAX_LOGIN_ATTEMPTS;
    }

    @Override
    public boolean isIpAllowed(IpAddress ipAddress) {
        String ip = ipAddress.getValue();

        if (securityCacheManager.isIpWhitelisted(ip)) {
            return true;
        }

        return !securityCacheManager.isIpBlacklisted(ip);
    }

    @Override
    public boolean isLoginTimeAllowed() {
        return true;
    }

    @Override
    public boolean isDeviceAllowed(String userAgent) {
        return true;
    }

    @Override
    public long recordLoginFailure(String username) {
        return securityCacheManager.recordLoginFailure(username);
    }

    @Override
    public void resetLoginFailure(String username) {
        securityCacheManager.resetLoginFailure(username);
    }

    @Override
    public long getLoginFailures(String username) {
        return securityCacheManager.getLoginFailures(username);
    }

    public void addIpToBlacklist(String ip) {
        securityCacheManager.addIpToBlacklist(ip);
    }

    public void removeIpFromBlacklist(String ip) {
        securityCacheManager.removeIpFromBlacklist(ip);
    }
}
