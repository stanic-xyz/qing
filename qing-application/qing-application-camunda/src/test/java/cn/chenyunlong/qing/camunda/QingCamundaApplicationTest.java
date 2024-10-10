package cn.chenyunlong.qing.camunda;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(
    // ...其他参数...
    properties = {
        "camunda.bpm.generate-unique-process-engine-name=true",
        // 这只在 SpringBootProcessApplication 中需要
        // 用于测试
        "camunda.bpm.generate-unique-process-application-name=true",
        "spring.datasource.generate-unique-name=true",
    },
    classes = QingCamundaApplication.class
)
public class QingCamundaApplicationTest {

    @Test
    public void contextLoads() {

    }
}
