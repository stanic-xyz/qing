package cn.chenyunlong.qing.auth.application.service;

import cn.chenyunlong.qing.auth.config.TestConfig;
import cn.chenyunlong.qing.auth.domain.authentication.cache.SecurityCacheManager;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.IpAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 安全策略集成测试
 * 验证密码策略、IP白名单等核心功能
 */
@SpringBootTest(classes = {SecurityPolicyService.class})
@ActiveProfiles("test")
@Import(TestConfig.class)
class SecurityPolicyIntegrationTest {

    @Autowired
    private SecurityPolicyService securityPolicyService;

    @Autowired
    private SecurityCacheManager securityCacheManager;

    @BeforeEach
    void setUp() throws Exception {
        // 通过反射清空内存缓存
        Field ipBlacklistField = securityCacheManager.getClass().getDeclaredField("ipBlacklist");
        ipBlacklistField.setAccessible(true);
        ((java.util.Set<?>) ipBlacklistField.get(securityCacheManager)).clear();

        Field ipWhitelistField = securityCacheManager.getClass().getDeclaredField("ipWhitelist");
        ipWhitelistField.setAccessible(true);
        ((java.util.Set<?>) ipWhitelistField.get(securityCacheManager)).clear();

        Field loginFailuresField = securityCacheManager.getClass().getDeclaredField("loginFailures");
        loginFailuresField.setAccessible(true);
        ((java.util.Map<?, ?>) loginFailuresField.get(securityCacheManager)).clear();
    }

    @Test
    @DisplayName("测试IP白名单：IP不在白名单且不在黑名单中时，允许访问")
    void isIpAllowed_Default_Allowed() {
        String ip = "192.168.1.100";
        IpAddress ipAddress = IpAddress.of(ip);

        boolean result = securityPolicyService.isIpAllowed(ipAddress);

        assertTrue(result);
    }

    @Test
    @DisplayName("测试IP白名单：IP不在白名单但在黑名单中时，拒绝访问")
    void isIpAllowed_Blacklist_Denied() {
        String ip = "192.168.1.100";
        IpAddress ipAddress = IpAddress.of(ip);

        securityCacheManager.addIpToBlacklist("192.168.1.100");

        boolean result = securityPolicyService.isIpAllowed(ipAddress);

        assertFalse(result);
    }

    @Test
    @DisplayName("测试IP黑名单：IP从黑名单移除后，允许访问")
    void isIpAllowed_BlacklistRemoved_Allowed() {
        String ip = "192.168.1.100";
        IpAddress ipAddress = IpAddress.of(ip);

        securityCacheManager.addIpToBlacklist("192.168.1.100");
        securityCacheManager.removeIpFromBlacklist("192.168.1.100");

        boolean result = securityPolicyService.isIpAllowed(ipAddress);

        assertTrue(result);
    }

    @Test
    @DisplayName("测试记录登录失败")
    void recordLoginFailure() {
        String username = "test_user_record_failure_" + System.currentTimeMillis();

        long failures = securityPolicyService.recordLoginFailure(username);

        assertEquals(1, failures);
    }

    @Test
    @DisplayName("测试重置登录失败")
    void resetLoginFailure() {
        String username = "test_user_reset_" + System.currentTimeMillis();

        securityPolicyService.recordLoginFailure(username);
        securityPolicyService.recordLoginFailure(username);
        securityPolicyService.resetLoginFailure(username);

        long failures = securityPolicyService.getLoginFailures(username);
        assertEquals(0, failures);
    }

    @Test
    @DisplayName("测试获取登录失败次数")
    void getLoginFailures() {
        String username = "test_user_get_failures_" + System.currentTimeMillis();

        securityPolicyService.recordLoginFailure(username);
        securityPolicyService.recordLoginFailure(username);
        securityPolicyService.recordLoginFailure(username);

        long failures = securityPolicyService.getLoginFailures(username);

        assertEquals(3, failures);
    }

    @Test
    @DisplayName("测试获取登录失败次数-无记录")
    void getLoginFailures_NoRecord() {
        String username = "test_user_no_record_" + System.currentTimeMillis();

        long failures = securityPolicyService.getLoginFailures(username);

        assertEquals(0, failures);
    }
}
