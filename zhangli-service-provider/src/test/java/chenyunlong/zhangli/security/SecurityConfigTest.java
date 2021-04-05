package chenyunlong.zhangli.security;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityConfigTest {

    @Test
    public void testPasswordEncoder() {

        String username = "stan";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoded = new BCryptPasswordEncoder().encode(username);

        System.out.println("encoded：" + encoded);
        Assert.assertTrue(encoder.matches(username, encoded));

    }
}
