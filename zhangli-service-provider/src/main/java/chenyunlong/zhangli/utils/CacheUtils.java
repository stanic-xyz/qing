package chenyunlong.zhangli.utils;

import chenyunlong.zhangli.utils.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Set;

/**
 * Cache工具类
 *
 * @author ruoyi
 */
@SuppressWarnings({"unused"})
public class CacheUtils {
    private static final Logger logger = LoggerFactory.getLogger(CacheUtils.class);

    private static final CacheManager cacheManager = SpringUtils.getBean(CacheManager.class);

    private static final String SYS_CACHE = "sys-cache";

    /**
     * 获取SYS_CACHE缓存
     * 使用的时候我们应该要知道我们缓存了什么东西
     *
     * @param key 缓存键
     * @return 缓存信息
     */
    public static Object get(String key) {
        return get(SYS_CACHE, key);
    }

    /**
     * 获取SYS_CACHE缓存
     *
     * @param key          缓存键
     * @param defaultValue 默认值
     * @return 缓存信息
     */
    public static Object get(String key, Object defaultValue) {
        Object value = get(key);
        return value != null ? value : defaultValue;
    }

    /**
     * 写入SYS_CACHE缓存
     *
     * @param key 缓存键
     */
    public static void put(String key, Object value) {
        put(SYS_CACHE, key, value);
    }

    /**
     * 从SYS_CACHE缓存中移除
     *
     * @param key 缓存键
     */
    public static void remove(String key) {
        remove(SYS_CACHE, key);
    }

    /**
     * 获取缓存
     *
     * @param cacheName 缓存名称
     * @param key       缓存键
     * @return 缓存信息
     */
    public static Object get(String cacheName, String key) {
        return getCache(cacheName).get(getKey(key));
    }

    /**
     * 获取缓存
     *
     * @param cacheName    缓存名称
     * @param key          缓存的键
     * @param defaultValue 默认值
     * @return 缓存信息
     */
    public static Object get(String cacheName, String key, Object defaultValue) {
        Object value = get(cacheName, getKey(key));
        return value != null ? value : defaultValue;
    }

    /**
     * 写入缓存
     *
     * @param cacheName 缓存名称
     * @param key       缓存的键
     * @param value     缓存的值
     */
    public static void put(String cacheName, String key, Object value) {
        getCache(cacheName).put(getKey(key), value);
    }

    /**
     * 从缓存中移除
     *
     * @param cacheName 缓存名称
     * @param key       缓存键
     */
    public static void remove(String cacheName, String key) {
        getCache(cacheName).evict(getKey(key));
    }

    /**
     * 从缓存中移除所有
     *
     * @param cacheName 缓存名称
     */
    public static void removeAll(String cacheName) {
        Cache cache = getCache(cacheName);
        cache.clear();
        logger.info("清理缓存：=> => {} ", cacheName);
    }

    /**
     * 从缓存中移除指定key
     *
     * @param keys 多个缓存键
     */
    public static void removeByKeys(Set<String> keys) {
        removeByKeys(SYS_CACHE, keys);
    }

    /**
     * 从缓存中移除指定key
     *
     * @param cacheName 缓存名称
     * @param keys      多个缓存键
     */
    public static void removeByKeys(String cacheName, Set<String> keys) {
        for (String key : keys) {
            remove(key);
        }
        logger.info("清理缓存： {} => {}", cacheName, keys);
    }

    /**
     * 获取缓存键名
     *
     * @param key 缓存的key
     * @return 键
     */
    private static String getKey(String key) {
        return key;
    }

    /**
     * 获得一个Cache，没有则显示日志。
     *
     * @param cacheName 缓存名称
     * @return 缓存
     */
    public static Cache getCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            throw new RuntimeException("当前系统中没有定义“" + cacheName + "”这个缓存。");
        }
        return cache;
    }

    /**
     * 获取所有缓存
     *
     * @return 缓存组
     */
    public static String[] getCacheNames() {
        return cacheManager.getCacheNames().toArray(new String[0]);
    }
}
