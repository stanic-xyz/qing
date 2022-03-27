package chenyunlong.zhangli;

import chenyunlong.zhangli.controller.BaseApiTest;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;

import javax.annotation.Resource;

public class RedisTest extends BaseApiTest {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis() {
        stringRedisTemplate.opsForValue().set("name", "team");
        String name = stringRedisTemplate.opsForValue().get("name");
        Assert.isTrue("team".equalsIgnoreCase(name));
    }
}
