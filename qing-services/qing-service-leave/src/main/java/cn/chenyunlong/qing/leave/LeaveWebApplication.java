package cn.chenyunlong.qing.leave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class LeaveWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeaveWebApplication.class, args);
    }
}
