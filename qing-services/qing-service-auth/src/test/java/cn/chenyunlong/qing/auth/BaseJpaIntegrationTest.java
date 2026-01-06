package cn.chenyunlong.qing.auth;

import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;

/**
 * 登录功能集成测试
 * 验证用户登录后JWT令牌中包含准确的角色和权限信息
 */
@ActiveProfiles("test")
@DataJpaTest(excludeAutoConfiguration = RedisAutoConfiguration.class)
@ComponentScans(value = {
        @ComponentScan("cn.chenyunlong.qing.auth.infrastructure.repository"),
        @ComponentScan("cn.chenyunlong.qing.auth.infrastructure.converter"),
        @ComponentScan("cn.chenyunlong.qing.auth.application.service"),
        @ComponentScan("cn.chenyunlong.qing.auth.domain.*.service"),
        @ComponentScan("cn.chenyunlong.qing.auth.domain.*.specification"),
})
@EnableJpaRepositories(basePackages = "cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository")
@EntityScan(basePackages = "cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity")
public class BaseJpaIntegrationTest {
}
