package cn.chenyunlong.qing.service.llm.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * LLM 解析器配置类
 */
@Configuration
@Slf4j
public class LlmParserConfig {

    /**
     * 配置 Caffeine 缓存管理器
     */
    @Bean
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        // 系统上下文缓存 - 30分钟
        cacheManager.registerCache("llm-parse-context",
                Caffeine.newBuilder()
                        .expireAfterWrite(30, TimeUnit.MINUTES)
                        .maximumSize(100)
                        .recordStats()
                        .build());

        // 解析结果缓存 - 7天
        cacheManager.registerCache("llm-parse-result",
                Caffeine.newBuilder()
                        .expireAfterWrite(7, TimeUnit.DAYS)
                        .maximumSize(1000)
                        .recordStats()
                        .build());

        log.info("Initialized Caffeine cache manager for LLM parser");
        return cacheManager;
    }

    /**
     * 配置默认缓存管理器别名
     */
    @Bean
    public CaffeineCacheManager cacheManager() {
        return new CaffeineCacheManager();
    }
}
