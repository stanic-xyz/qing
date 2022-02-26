package chenyunlong.zhangli;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class TestLog4J {

    @Test
    public void testLog4J() {
        String message = "${java:os}";
        log.info("message:{}", message);
    }
}
