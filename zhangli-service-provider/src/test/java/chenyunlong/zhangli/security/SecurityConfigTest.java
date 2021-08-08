package chenyunlong.zhangli.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;

public class SecurityConfigTest {

    @Test
    public void testPasswordEncoder() {

        String username = "stan";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoded = new BCryptPasswordEncoder().encode(username);

        System.out.println("encoded：" + encoded);
        Assert.isTrue(encoder.matches(username, encoded), "消息不符合");

    }
}
