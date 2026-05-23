package cn.chenyunlong.qing.auth.config;

import cn.chenyunlong.qing.auth.domain.authentication.cache.SecurityCacheManager;
import cn.chenyunlong.qing.auth.infrastructure.cache.InMemorySecurityCacheManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public SecurityCacheManager testSecurityCacheManager() {
        return new InMemorySecurityCacheManager();
    }
}
