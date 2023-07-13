package cn.chenyunlong.qing;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(classes = UtApplication.class)
public class QingEurekaServiceApplicationTests {

    @Test
    public void contextLoads() {
        log.info("应用启动成功，测试成功");
    }
}
