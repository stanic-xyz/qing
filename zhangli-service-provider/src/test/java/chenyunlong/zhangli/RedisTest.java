package chenyunlong.zhangli;

import ai.grakn.redismock.RedisServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.IOException;

@ActiveProfiles("test")
@SpringBootTest
public class RedisTest {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static RedisServer redisServer;

    @BeforeAll
    public static void init() {
        try {
            redisServer = RedisServer.newRedisServer();
            redisServer.start();
            //因为RedisServer启动的端口无法预知（如果写死的话就容易有端口冲突），所以需要实现动态端口配置。
            System.setProperty("spring.redis.port", Integer.toString(redisServer.getBindPort()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    public static void close() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    @Test
    public void testRedis() {
        stringRedisTemplate.opsForValue().set("name", "team");
        String name = stringRedisTemplate.opsForValue().get("name");
        Assert.isTrue("team".equalsIgnoreCase(name), "不符合匹配");
    }
}
