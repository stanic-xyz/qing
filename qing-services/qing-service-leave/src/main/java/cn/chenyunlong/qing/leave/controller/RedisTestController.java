package cn.chenyunlong.qing.leave.controller;

import jakarta.annotation.Resource;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class RedisTestController {

    @Resource(name = "masterRedisTemplate")
    private RedisTemplate<String, Object> masterRedisTemplate;

    @Resource(name = "slaveRedisTemplate")
    private RedisTemplate<String, Object> slaveRedisTemplate;

    @GetMapping("/test-master-write")
    public String testMasterWrite() {
        masterRedisTemplate.opsForValue().set("test-key", "written by master");
        return "Data written to master (port 6379)";
    }

    @GetMapping("/test-slave-read")
    public String testSlaveRead() {
        String value = (String) slaveRedisTemplate.opsForValue().get("test-key");
        return "Read from slave (port 6380): " + value;
    }
}
