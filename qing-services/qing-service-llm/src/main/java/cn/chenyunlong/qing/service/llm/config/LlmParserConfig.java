package cn.chenyunlong.qing.service.llm.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

/**
 * LLM 解析器配置类
 */
@Configuration
@Slf4j
public class LlmParserConfig {

    public static final String CACHE_NAME_PARSE_CONTEXT = "llm-parse-context";
    public static final String CACHE_NAME_PARSE_RESULT = "llm-parse-result";

    /**
     * 配置 Caffeine 缓存管理器
     */
    @Bean
    @Primary
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCacheNames(java.util.Arrays.asList(
                CACHE_NAME_PARSE_CONTEXT,
                CACHE_NAME_PARSE_RESULT
        ));
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .maximumSize(100)
                .recordStats());
        log.info("Initialized Caffeine cache manager for LLM parser");
        return cacheManager;
    }

    /**
     * 解析结果缓存管理器（7天TTL）
     */
    @Bean("resultCacheManager")
    public CacheManager resultCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(CACHE_NAME_PARSE_RESULT);
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(7, TimeUnit.DAYS)
                .maximumSize(1000)
                .recordStats());
        return cacheManager;
    }
}
