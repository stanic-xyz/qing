package cn.chenyunlong.qing.leave.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.redis.master")
    public RedisProperties masterProperties() {
        return new RedisProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.redis.slave")
    public RedisProperties slaveProperties() {
        return new RedisProperties();
    }

    @Primary // 默认使用主节点
    @Bean(name = "masterRedisTemplate")
    public RedisTemplate<String, Object> masterRedisTemplate(@Qualifier("masterProperties") RedisProperties masterProperties) {
        return createRedisTemplate(masterProperties);
    }

    @Bean(name = "slaveRedisTemplate")
    public RedisTemplate<String, Object> slaveRedisTemplate(@Qualifier("slaveProperties") RedisProperties slaveProperties) {
        return createRedisTemplate(slaveProperties);
    }

    private RedisTemplate<String, Object> createRedisTemplate(RedisProperties properties) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(properties.getHost());
        config.setPort(properties.getPort());
        if (properties.getPassword() != null) {
            config.setPassword(properties.getPassword());
        }

        LettuceConnectionFactory factory = new LettuceConnectionFactory(config);
        factory.afterPropertiesSet();

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        // 使用String序列化器，方便查看
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
