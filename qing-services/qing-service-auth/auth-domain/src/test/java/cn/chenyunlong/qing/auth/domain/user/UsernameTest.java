package cn.chenyunlong.qing.auth.domain.user;

import cn.chenyunlong.qing.auth.domain.user.valueObject.Username;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsernameTest {

    /**
     * 校验用户名规则与接口层一致
     */
    @Test
    @DisplayName("用户名规则：长度与字符集")
    void testUsernameRules() {
        assertDoesNotThrow(() -> Username.of("Alice-01"));
        assertDoesNotThrow(() -> Username.of("Bob_123"));
        // 开头不能为数字
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> Username.of("1start"));
        assertTrue(ex1.getMessage().contains("用户名不能以数字开头"));
        // 非法字符
        assertThrows(IllegalArgumentException.class, () -> Username.of("bad!name"));
        // 长度过短
        assertThrows(IllegalArgumentException.class, () -> Username.of("ab"));
        // 长度过长（51）
        assertThrows(IllegalArgumentException.class, () -> Username.of("a".repeat(51)));
    }
}
