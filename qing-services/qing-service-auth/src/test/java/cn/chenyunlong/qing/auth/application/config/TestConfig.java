package cn.chenyunlong.qing.auth.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 测试专用配置，排除 AdminAccountService 等无关 Bean
 */
@TestConfiguration
@ComponentScan(basePackages = {
        "cn.chenyunlong.qing.auth.application",
        "cn.chenyunlong.qing.auth.domain",
        "cn.chenyunlong.qing.infrastructure.jpa.memory"
})
public class TestConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
