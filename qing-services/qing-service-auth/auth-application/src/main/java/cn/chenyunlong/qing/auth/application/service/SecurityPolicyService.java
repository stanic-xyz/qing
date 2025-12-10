package cn.chenyunlong.qing.auth.application.service;

import cn.chenyunlong.qing.auth.domain.authentication.valueObject.IpAddress;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.PasswordExpiredHandlePolicy;
import cn.chenyunlong.qing.auth.domain.user.port.SecurityPolicyPort;
import org.springframework.stereotype.Service;

@Service
public class SecurityPolicyService implements SecurityPolicyPort {

    private static final Integer DEFAULT_MAX_LOGIN_ATTEMPTS = 5;

    public PasswordExpiredHandlePolicy getPasswordExpiredPolicy() {
        return PasswordExpiredHandlePolicy.FAIL;
    }

    @Override
    public int getMaxLoginAttempts() {
        return DEFAULT_MAX_LOGIN_ATTEMPTS;
    }

    @Override
    public boolean isIpAllowed(IpAddress ipAddress) {
        return true;
    }

    public boolean isLoginTimeAllowed() {
        return true;
    }

    public boolean isDeviceAllowed(String userAgent) {
        return true;
    }

}
