package cn.chenyunlong.qing.auth.domain.user.port;

import cn.chenyunlong.qing.auth.domain.authentication.valueObject.IpAddress;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.PasswordExpiredHandlePolicy;

/**
 * 策略端口：供规约使用的防护策略
 */
public interface SecurityPolicyPort {
    boolean isIpAllowed(IpAddress ipAddress);

    boolean isLoginTimeAllowed();

    boolean isDeviceAllowed(String userAgent);

    PasswordExpiredHandlePolicy getPasswordExpiredPolicy();

    int getMaxLoginAttempts();

    void resetLoginFailure(String value);

    long recordLoginFailure(String value);

    long getLoginFailures(String value);
}
