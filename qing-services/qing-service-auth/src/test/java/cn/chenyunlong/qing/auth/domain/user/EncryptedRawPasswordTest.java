package cn.chenyunlong.qing.auth.domain.user;

import cn.chenyunlong.qing.auth.domain.user.valueObject.EncryptedPassword;
import cn.chenyunlong.qing.auth.domain.user.valueObject.RawPassword;
import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import cn.chenyunlong.qing.auth.domain.user.valueObject.Username;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EncryptedRawPasswordTest {

    /**
     * 验证 BCrypt 加密与匹配
     */
    @Test
    @DisplayName("BCrypt 加密与匹配校验")
    void testBcryptEncryptAndMatch() {
        String plain = "Abc12345!";
        EncryptedPassword encrypted = EncryptedPassword.of(plain);

        assertNotEquals(plain, encrypted.value());
        assertTrue(encrypted.matches(plain));
        assertFalse(encrypted.matches("wrongPass1!"));
    }

    /**
     * 验证用户密码校验逻辑（BCrypt）
     */
    @Test
    @DisplayName("用户密码校验")
    void testUserVerifyPassword() {
        String plain = "Abc12345!";
        User user = User.create(UserId.of(1L), Username.of("Test_User"), RawPassword.of(plain), "nick");

        assertTrue(user.verifyPassword(EncryptedPassword.ofEncrypted(plain)));
        assertFalse(user.verifyPassword(EncryptedPassword.ofEncrypted("Wrong123!")));
    }
}
