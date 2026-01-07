package cn.chenyunlong.qing.auth;

import cn.chenyunlong.qing.auth.domain.authentication.repository.TokenCacheRepository;
import cn.chenyunlong.qing.auth.domain.event.DomainEventPublisher;
import cn.chenyunlong.qing.auth.infrastructure.repository.redis.RedisSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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

    @MockitoBean
    private RedisSessionRepository redisSessionRepository;

    @MockitoBean
    private TokenCacheRepository tokenCacheRepository;

    @MockitoBean
    private StringRedisTemplate stringRedisTemplate;

    @MockitoBean
    RedisTemplate<String, Object> redisTemplate;

    @MockitoBean
    private DomainEventPublisher domainEventPublisher;

    @MockitoBean
    private ValueOperations<String, String> stringSetOperations;

    @MockitoBean
    private SetOperations<String, String> setOperations;

    @MockitoBean
    private ValueOperations<String, Object> stringObjectSetOperations;

    @MockitoBean
    private SetOperations<String, Object> setObjectOperations;

    @BeforeEach
    void setUp() {
        Mockito.when(stringRedisTemplate.opsForValue()).thenReturn(stringSetOperations);
        Mockito.when(stringRedisTemplate.opsForSet()).thenReturn(setOperations);
        Mockito.when(redisTemplate.opsForSet()).thenReturn(setObjectOperations);
    }
}
