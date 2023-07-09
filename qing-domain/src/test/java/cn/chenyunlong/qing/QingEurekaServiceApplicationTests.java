package cn.chenyunlong.qing;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = UtApplication.class)
public class QingEurekaServiceApplicationTests {

    @Test
    public void contextLoads() {
    }

}
