package cn.chenyunlong.qing.auth.application.service;

import cn.chenyunlong.qing.auth.domain.authentication.valueObject.IpAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {SecurityPolicyService.class})
@ActiveProfiles("test")
class SecurityPolicyIntegrationTest {

    @Autowired
    private SecurityPolicyService securityPolicyService;

    @MockitoBean
    private StringRedisTemplate redisTemplate;

    @Mock
    private SetOperations<String, String> setOperations;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private static final String IP_WHITELIST_KEY = "auth:security:ip:whitelist";
    private static final String IP_BLACKLIST_KEY = "auth:security:ip:blacklist";
    private static final String LOGIN_FAIL_PREFIX = "auth:security:login:fail:";

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForSet()).thenReturn(setOperations);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("测试IP白名单：当白名单不为空且IP在白名单中时，允许访问")
    void isIpAllowed_Whitelist_Allowed() {
        // Arrange
        String ip = "192.168.1.100";
        IpAddress ipAddress = IpAddress.of(ip);

        when(setOperations.size(IP_WHITELIST_KEY)).thenReturn(1L);
        when(setOperations.isMember(IP_WHITELIST_KEY, ip)).thenReturn(true);

        // Act
        boolean result = securityPolicyService.isIpAllowed(ipAddress);

        // Assert
        assertTrue(result);
        verify(setOperations).size(IP_WHITELIST_KEY);
        verify(setOperations).isMember(IP_WHITELIST_KEY, ip);
    }

    @Test
    @DisplayName("测试IP白名单：当白名单不为空且IP不在白名单中时，拒绝访问")
    void isIpAllowed_Whitelist_Denied() {
        // Arrange
        String ip = "192.168.1.100";
        IpAddress ipAddress = IpAddress.of(ip);

        when(setOperations.size(IP_WHITELIST_KEY)).thenReturn(1L);
        when(setOperations.isMember(IP_WHITELIST_KEY, ip)).thenReturn(false);

        // Act
        boolean result = securityPolicyService.isIpAllowed(ipAddress);

        // Assert
        assertFalse(result);
        verify(setOperations).size(IP_WHITELIST_KEY);
        verify(setOperations).isMember(IP_WHITELIST_KEY, ip);
    }

    @Test
    @DisplayName("测试IP黑名单：当白名单为空且IP在黑名单中时，拒绝访问")
    void isIpAllowed_Blacklist_Denied() {
        // Arrange
        String ip = "192.168.1.100";
        IpAddress ipAddress = IpAddress.of(ip);

        when(setOperations.size(IP_WHITELIST_KEY)).thenReturn(0L);
        when(setOperations.isMember(IP_BLACKLIST_KEY, ip)).thenReturn(true);

        // Act
        boolean result = securityPolicyService.isIpAllowed(ipAddress);

        // Assert
        assertFalse(result);
        verify(setOperations).size(IP_WHITELIST_KEY);
        verify(setOperations).isMember(IP_BLACKLIST_KEY, ip);
    }

    @Test
    @DisplayName("测试IP默认允许：当白名单为空且IP不在黑名单中时，允许访问")
    void isIpAllowed_Default_Allowed() {
        // Arrange
        String ip = "192.168.1.100";
        IpAddress ipAddress = IpAddress.of(ip);

        when(setOperations.size(IP_WHITELIST_KEY)).thenReturn(0L);
        when(setOperations.isMember(IP_BLACKLIST_KEY, ip)).thenReturn(false);

        // Act
        boolean result = securityPolicyService.isIpAllowed(ipAddress);

        // Assert
        assertTrue(result);
        verify(setOperations).size(IP_WHITELIST_KEY);
        verify(setOperations).isMember(IP_BLACKLIST_KEY, ip);
    }

    @Test
    @DisplayName("测试记录登录失败")
    void recordLoginFailure() {
        // Arrange
        String username = "test_user";
        String key = LOGIN_FAIL_PREFIX + username;

        when(valueOperations.increment(key)).thenReturn(1L);

        // Act
        long failures = securityPolicyService.recordLoginFailure(username);

        // Assert
        assertEquals(1L, failures);
        verify(valueOperations).increment(key);
        verify(redisTemplate).expire(eq(key), any(Duration.class));
    }

    @Test
    @DisplayName("测试重置登录失败")
    void resetLoginFailure() {
        // Arrange
        String username = "test_user";
        String key = LOGIN_FAIL_PREFIX + username;

        // Act
        securityPolicyService.resetLoginFailure(username);

        // Assert
        verify(redisTemplate).delete(key);
    }

    @Test
    @DisplayName("测试获取登录失败次数")
    void getLoginFailures() {
        // Arrange
        String username = "test_user";
        String key = LOGIN_FAIL_PREFIX + username;

        when(valueOperations.get(key)).thenReturn("3");

        // Act
        long failures = securityPolicyService.getLoginFailures(username);

        // Assert
        assertEquals(3L, failures);
        verify(valueOperations).get(key);
    }

    @Test
    @DisplayName("测试获取登录失败次数-无记录")
    void getLoginFailures_NoRecord() {
        // Arrange
        String username = "test_user";
        String key = LOGIN_FAIL_PREFIX + username;

        when(valueOperations.get(key)).thenReturn(null);

        // Act
        long failures = securityPolicyService.getLoginFailures(username);

        // Assert
        assertEquals(0L, failures);
        verify(valueOperations).get(key);
    }
}
