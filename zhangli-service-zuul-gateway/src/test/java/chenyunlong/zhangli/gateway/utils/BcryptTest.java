package chenyunlong.zhangli.gateway.utils;

import cn.hutool.crypto.digest.BCrypt;
import org.junit.Test;

/**
 * @author 陈云龙
 * @date 2020/10/23
 **/
public class BcryptTest {

    @Test
    public void cryptTest() {
        String cryptPassword = BCrypt.hashpw("opentest", BCrypt.gensalt());
        System.out.printf("Crypt password: [{}]%n", cryptPassword);
    }
}
