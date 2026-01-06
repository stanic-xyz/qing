package cn.chenyunlong.qing.auth.domain.user.specification;

import cn.chenyunlong.qing.auth.domain.authentication.Authentication;
import cn.chenyunlong.qing.auth.domain.authentication.exception.AuthenticationException;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.AuthFailureReason;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.IpAddress;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.PasswordExpiredHandlePolicy;
import cn.chenyunlong.qing.auth.domain.user.User;
import cn.chenyunlong.qing.auth.domain.user.port.SecurityPolicyPort;
import cn.chenyunlong.qing.auth.domain.user.repository.UserRepository;
import cn.chenyunlong.qing.auth.domain.user.valueObject.EncryptedPassword;
import cn.chenyunlong.qing.auth.domain.user.valueObject.Username;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthenticationSpecification {

    private final UserRepository userRepository;
    private final SecurityPolicyPort securityPolicyPort;

    /**
     * 用户登录规约检查（状态、锁定、密码）
     */
    public void check(User user, EncryptedPassword password) {
        checkAccountLockSpecification(user);
        checkUserStatusSpecification(user);
    }

    /**
     * 用户登录规约检查（含密码）
     *
     * @param user     用户
     * @param password 明文密码值对象
     */
    public void check(User user, EncryptedPassword password, IpAddress ipAddress, String userAgent) {
        checkAccountLockSpecification(user);
        checkUserStatusSpecification(user);

        // 检查IP是否允许
        if (!securityPolicyPort.isIpAllowed(ipAddress)) {
            throw new AuthenticationException("当前IP地址不允许登录");
        }

        // 检查登录失败次数 (从Redis获取)
        Username username = user.getUsername();
        long failures = securityPolicyPort.getLoginFailures(username.value());
        if (failures >= securityPolicyPort.getMaxLoginAttempts()) {
            throw new AuthenticationException("重试次数过多，账户已暂时锁定，请稍后再试");
        }

        // 密码验证
        if (!user.verifyPassword(password)) {
            // 记录失败次数
            long currentFailures = securityPolicyPort.recordLoginFailure(username.value());
            long remaining = securityPolicyPort.getMaxLoginAttempts() - currentFailures;
            throw new AuthenticationException("用户名或密码错误，剩余重试次数: " + (remaining > 0 ? remaining : 0));
        }

        // 登录成功，重置失败次数
        securityPolicyPort.resetLoginFailure(username.value());

        if (!securityPolicyPort.isLoginTimeAllowed()) {
            throw new AuthenticationException("当前时间段不允许登录");
        }
        if (!securityPolicyPort.isDeviceAllowed(userAgent)) {
            throw new AuthenticationException("当前设备不允许登录");
        }

        // 检查密码是否过期
        if (user.isPasswordExpired()) {
            PasswordExpiredHandlePolicy passwordExpiredPolicy = securityPolicyPort.getPasswordExpiredPolicy();
            if (passwordExpiredPolicy == PasswordExpiredHandlePolicy.FAIL) {
                // authentication.fail(AuthFailureReason.PASSWORD_EXPIRED);
                throw new AuthenticationException("密码已过期，请修改密码");
            }
            // todo 这里需要下发消息给用户，让TA修改密码
            log.warn("您的密码已过期，请更改密码");
        }
    }

    public void checkActiveCondition(User user) {
        // todo 检查用户激活的流程
        if (user.isActive()) {
            throw new IllegalArgumentException("用户已激活，无需再次激活！");
        }
    }

    /**
     * 检查管理员激活用户时的激活条件
     *
     * @param user          需要检查的用户对象
     * @param adminUsername 管理员用户名
     * @throws IllegalArgumentException 如果用户已经激活，则抛出此异常
     */
    public void checkActiveConditionForAdminActivation(User user, String adminUsername) {
        // 检查用户是否已经激活
        if (user.isActive()) {
            // 如果用户已激活，抛出异常提示无需再次激活
            throw new IllegalArgumentException("用户已激活，无需再次激活！");
        }

        checkAdminPermissions(adminUsername);
    }

    /**
     * 检查管理员用户时的激活条件
     *
     * @param user          需要检查的用户对象
     * @param adminUsername 管理员用户名
     * @throws IllegalArgumentException 如果用户已经激活，则抛出此异常
     */
    public void checkDeActiveConditionForAdminActivation(User user, String adminUsername) {
        // 检查用户是否已经激活
        if (!user.isActive()) {
            // 如果用户已激活，抛出异常提示无需再次激活
            throw new IllegalArgumentException("用户未激活，无法反激活！");
        }
        checkAdminPermissions(adminUsername);
    }

    private void checkAdminPermissions(String adminUsername) {
        // todo 检查管理员权限
    }

    /**
     * 规约1: 用户状态检查
     */
    private void checkUserStatusSpecification(User user) {
        if (!user.isActive()) {
            throw new AuthenticationException("用户未激活");
        }

        if (user.isSuspended()) {
            throw new AuthenticationException("用户已被暂停");
        }

        if (user.isDeleted()) {
            throw new AuthenticationException("用户已删除");
        }
    }

    /**
     * 规约2: 账户锁定检查
     */
    private void checkAccountLockSpecification(User user) {
        if (user.isLocked()) {
            throw new AuthenticationException("账户已被锁定");
        }
    }

    /**
     * 规约3: 密码验证
     */
    private void checkPasswordSpecification(User user, EncryptedPassword password, Authentication authentication) {
        if (!user.verifyPassword(password)) {
            // 密码错误，增加失败计数
            user.incrementLoginAttempts();
            userRepository.save(user);

            authentication.fail(AuthFailureReason.INVALID_CREDENTIALS);
            throw new AuthenticationException("用户名或密码错误");
        }

        // 密码正确，重置失败计数
        if (user.getLoginAttempts() > 0) {
            user.resetLoginAttempts();
            userRepository.save(user);
        }
    }
}
