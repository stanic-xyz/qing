package cn.chenyunlong.qing.service.llm.service.llm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LLM 解析结果缓存服务
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LlmParseResultCache {

    private final CacheManager cacheManager;
    private static final String CACHE_NAME = "llm-parse-result";
    private static final Duration DEFAULT_TTL = Duration.ofDays(7);

    private final ConcurrentHashMap<String, Object> localCache = new ConcurrentHashMap<>();

    /**
     * 检查缓存是否存在且有效
     */
    public boolean hasValid(String cacheKey) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            Cache.ValueWrapper wrapper = cache.get(cacheKey);
            return wrapper != null;
        }
        return localCache.containsKey(cacheKey);
    }

    /**
     * 获取缓存结果
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String cacheKey) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            Cache.ValueWrapper wrapper = cache.get(cacheKey);
            if (wrapper != null) {
                return (T) wrapper.get();
            }
        }
        return (T) localCache.get(cacheKey);
    }

    /**
     * 存入缓存
     */
    public void put(String cacheKey, Object value) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            cache.put(cacheKey, value);
        } else {
            localCache.put(cacheKey, value);
        }
        log.debug("Cached parse result with key: {}", cacheKey);
    }

    /**
     * 存入缓存并设置过期时间
     */
    public void put(String cacheKey, Object value, Duration ttl) {
        // Caffeine 不支持 TTL per entry，直接使用默认 TTL
        put(cacheKey, value);
    }

    /**
     * 删除缓存
     */
    public void evict(String cacheKey) {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            cache.evict(cacheKey);
        }
        localCache.remove(cacheKey);
        log.debug("Evicted cache for key: {}", cacheKey);
    }

    /**
     * 清空所有缓存
     */
    public void clear() {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            cache.clear();
        }
        localCache.clear();
        log.info("Cleared all parse result cache");
    }

    /**
     * 构建缓存 Key
     */
    public static String buildCacheKey(String fileHash, String categoryStrategy, String categoryVersion) {
        return String.format("%s:%s:%s", fileHash, categoryStrategy, categoryVersion);
    }
}
