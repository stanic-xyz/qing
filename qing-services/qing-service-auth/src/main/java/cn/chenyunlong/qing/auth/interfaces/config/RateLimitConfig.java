/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions username the Mulan PSL v2.
 * You may obtain a copy username Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.chenyunlong.qing.auth.interfaces.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 请求限流配置
 *
 * <p>实现多层次的请求限流机制：</p>
 * <ul>
 *   <li>基于IP的限流：防止单个IP过度请求</li>
 *   <li>基于用户的限流：防止单个用户过度请求</li>
 *   <li>基于接口的限流：防止特定接口过载</li>
 *   <li>全局限流：防止系统整体过载</li>
 * </ul>
 *
 * <p>支持的限流算法：</p>
 * <ul>
 *   <li>令牌桶算法：平滑限流，允许突发流量</li>
 *   <li>滑动窗口算法：精确限流，防止突发流量</li>
 *   <li>信号量限流：控制并发数量</li>
 * </ul>
 *
 * @author 陈云龙
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class RateLimitConfig {

    /**
     * Redis限流脚本
     * 使用Lua脚本保证原子性
     */
    private static final String RATE_LIMIT_SCRIPT =
            "local key = KEYS[1]\n" +
                    "local limit = tonumber(ARGV[1])\n" +
                    "local window = tonumber(ARGV[2])\n" +
                    "local current = redis.call('GET', key)\n" +
                    "if current == false then\n" +
                    "    redis.call('SET', key, 1)\n" +
                    "    redis.call('EXPIRE', key, window)\n" +
                    "    return {1, limit}\n" +
                    "else\n" +
                    "    current = tonumber(current)\n" +
                    "    if current < limit then\n" +
                    "        local newVal = redis.call('INCR', key)\n" +
                    "        local ttl = redis.call('TTL', key)\n" +
                    "        if ttl == -1 then\n" +
                    "            redis.call('EXPIRE', key, window)\n" +
                    "        end\n" +
                    "        return {newVal, limit}\n" +
                    "    else\n" +
                    "        local ttl = redis.call('TTL', key)\n" +
                    "        return {current, limit, ttl}\n" +
                    "    end\n" +
                    "end";

    /**
     * 滑动窗口限流脚本
     */
    private static final String SLIDING_WINDOW_SCRIPT =
            "local key = KEYS[1]\n" +
                    "local window = tonumber(ARGV[1])\n" +
                    "local limit = tonumber(ARGV[2])\n" +
                    "local now = tonumber(ARGV[3])\n" +
                    "local clearBefore = now - window\n" +
                    "redis.call('ZREMRANGEBYSCORE', key, 0, clearBefore)\n" +
                    "local current = redis.call('ZCARD', key)\n" +
                    "if current < limit then\n" +
                    "    redis.call('ZADD', key, now, now)\n" +
                    "    redis.call('EXPIRE', key, window)\n" +
                    "    return {1, current + 1, limit}\n" +
                    "else\n" +
                    "    return {0, current, limit}\n" +
                    "end";

    /**
     * Redis限流脚本Bean
     *
     * @return Redis脚本
     */
    @Bean
    public DefaultRedisScript<Long[]> rateLimitScript() {
        DefaultRedisScript<Long[]> script = new DefaultRedisScript<>();
        script.setScriptText(RATE_LIMIT_SCRIPT);
        script.setResultType(Long[].class);
        return script;
    }

    /**
     * 滑动窗口限流脚本Bean
     *
     * @return Redis脚本
     */
    @Bean
    public DefaultRedisScript<Long[]> slidingWindowScript() {
        DefaultRedisScript<Long[]> script = new DefaultRedisScript<>();
        script.setScriptText(SLIDING_WINDOW_SCRIPT);
        script.setResultType(Long[].class);
        return script;
    }

    /**
     * 限流服务
     *
     * @param redisTemplateProvider Redis模板
     * @param rateLimitScript       限流脚本
     * @param slidingWindowScript   滑动窗口脚本
     * @return 限流服务
     */
    @Bean
    public RateLimitService rateLimitService(ObjectProvider<RedisTemplate<String, Object>> redisTemplateProvider,
                                             @Qualifier("rateLimitScript") DefaultRedisScript<Long[]> rateLimitScript,
                                             @Qualifier("slidingWindowScript") DefaultRedisScript<Long[]> slidingWindowScript) {
        RedisTemplate<String, Object> redisTemplate = redisTemplateProvider.getIfAvailable();
        if (redisTemplate == null) {
            log.warn("RedisTemplate未发现，使用内存限流服务作为降级方案");
            return new InMemoryRateLimitService();
        }
        return new RateLimitService(redisTemplate, rateLimitScript, slidingWindowScript);
    }

    /**
     * 信号量限流器
     * 用于控制并发数量
     *
     * @return 信号量限流器
     */
    @Bean
    public SemaphoreRateLimiter semaphoreRateLimiter() {
        return new SemaphoreRateLimiter();
    }

    /**
     * 限流属性配置
     */
    @Setter
    @Getter
    @ConfigurationProperties(prefix = "qing.rate-limit")
    public static class RateLimitProperties {

        // getters and setters
        /**
         * 是否启用限流
         */
        private boolean enabled = true;

        /**
         * IP限流配置
         */
        private IpRateLimit ip = new IpRateLimit();

        /**
         * 用户限流配置
         */
        private UserRateLimit user = new UserRateLimit();

        /**
         * 接口限流配置
         */
        private ApiRateLimit api = new ApiRateLimit();

        /**
         * 全局限流配置
         */
        private GlobalRateLimit global = new GlobalRateLimit();

        /**
         * IP限流配置
         */
        @Setter
        @Getter
        public static class IpRateLimit {
            private boolean enabled = true;
            private int limit = 100; // 每分钟100次
            private int windowSeconds = 60; // 时间窗口60秒

        }

        /**
         * 用户限流配置
         */
        @Setter
        @Getter
        public static class UserRateLimit {
            private boolean enabled = true;
            private int limit = 200; // 每分钟200次
            private int windowSeconds = 60; // 时间窗口60秒

        }

        /**
         * 接口限流配置
         */
        @Setter
        @Getter
        public static class ApiRateLimit {
            private boolean enabled = true;
            private int loginLimit = 10; // 登录接口每分钟10次
            private int registerLimit = 5; // 注册接口每分钟5次
            private int windowSeconds = 60; // 时间窗口60秒

        }

        /**
         * 全局限流配置
         */
        @Setter
        @Getter
        public static class GlobalRateLimit {
            private boolean enabled = true;
            private int limit = 1000; // 全局每秒1000次
            private int windowSeconds = 1; // 时间窗口1秒
            private int concurrentLimit = 100; // 并发限制100
        }
    }

    /**
     * 限流服务实现
     */
    public static class RateLimitService {

        private final RedisTemplate<String, Object> redisTemplate;
        private final DefaultRedisScript<Long[]> rateLimitScript;
        private final DefaultRedisScript<Long[]> slidingWindowScript;

        public RateLimitService(RedisTemplate<String, Object> redisTemplate,
                                DefaultRedisScript<Long[]> rateLimitScript,
                                DefaultRedisScript<Long[]> slidingWindowScript) {
            this.redisTemplate = redisTemplate;
            this.rateLimitScript = rateLimitScript;
            this.slidingWindowScript = slidingWindowScript;
        }

        /**
         * 检查是否允许请求（固定窗口）
         *
         * @param key           限流键
         * @param limit         限制次数
         * @param windowSeconds 时间窗口（秒）
         * @return 限流结果
         */
        public RateLimitResult isAllowed(String key, int limit, int windowSeconds) {
            try {
                Long[] result = redisTemplate.execute(rateLimitScript,
                        Collections.singletonList(key), limit, windowSeconds);

                if (result != null && result.length >= 2) {
                    long current = result[0];
                    long maxLimit = result[1];
                    long ttl = result.length > 2 ? result[2] : windowSeconds;

                    return RateLimitResult.builder()
                            .allowed(current <= maxLimit)
                            .current(current)
                            .limit(maxLimit)
                            .remainingTime(ttl)
                            .build();
                }

                return RateLimitResult.allowed();
            } catch (Exception e) {
                log.error("限流检查失败: key={}, limit={}, window={}", key, limit, windowSeconds, e);
                // 限流服务异常时，允许请求通过
                return RateLimitResult.allowed();
            }
        }

        /**
         * 检查是否允许请求（滑动窗口）
         *
         * @param key           限流键
         * @param limit         限制次数
         * @param windowSeconds 时间窗口（秒）
         * @return 限流结果
         */
        public RateLimitResult isAllowedSlidingWindow(String key, int limit, int windowSeconds) {
            try {
                long now = System.currentTimeMillis();
                Long[] result = redisTemplate.execute(slidingWindowScript,
                        Collections.singletonList(key), windowSeconds * 1000L, limit, now);

                if (result != null && result.length >= 3) {
                    boolean allowed = result[0] == 1;
                    long current = result[1];
                    long maxLimit = result[2];

                    return RateLimitResult.builder()
                            .allowed(allowed)
                            .current(current)
                            .limit(maxLimit)
                            .remainingTime(windowSeconds)
                            .build();
                }

                return RateLimitResult.allowed();
            } catch (Exception e) {
                log.error("滑动窗口限流检查失败: key={}, limit={}, window={}", key, limit, windowSeconds, e);
                // 限流服务异常时，允许请求通过
                return RateLimitResult.allowed();
            }
        }

        /**
         * 生成限流键
         *
         * @param prefix     前缀
         * @param identifier 标识符
         * @return 限流键
         */
        public String generateKey(String prefix, String identifier) {
            return String.format("rate_limit:%s:%s", prefix, identifier);
        }
    }

    /**
     * 内存限流服务（降级方案）
     */
    public static class InMemoryRateLimitService extends RateLimitService {

        private final ConcurrentHashMap<String, WindowCounter> counters = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<String, ArrayDeque<Long>> windows = new ConcurrentHashMap<>();

        public InMemoryRateLimitService() {super(null, null, null);}

        @Override
        public RateLimitResult isAllowed(String key, int limit, int windowSeconds) {
            long now = System.currentTimeMillis();
            WindowCounter counter = counters.compute(key, (k, old) -> {
                if (old == null || now >= old.expiresAt) {
                    WindowCounter wc = new WindowCounter();
                    wc.count = 1;
                    wc.expiresAt = now + windowSeconds * 1000L;
                    return wc;
                } else {
                    old.count += 1;
                    return old;
                }
            });

            boolean allowed = counter.count <= limit;
            long remainingMillis = Math.max(0, counter.expiresAt - now);
            long remainingSeconds = TimeUnit.MILLISECONDS.toSeconds(remainingMillis);

            return RateLimitResult.builder()
                    .allowed(allowed)
                    .current(counter.count)
                    .limit(limit)
                    .remainingTime(remainingSeconds)
                    .build();
        }

        @Override
        public RateLimitResult isAllowedSlidingWindow(String key, int limit, int windowSeconds) {
            long now = System.currentTimeMillis();
            ArrayDeque<Long> deque = windows.computeIfAbsent(key, k -> new ArrayDeque<>());
            long threshold = now - windowSeconds * 1000L;
            while (!deque.isEmpty() && deque.peekFirst() < threshold) {
                deque.pollFirst();
            }

            boolean allowed;
            if (deque.size() < limit) {
                deque.addLast(now);
                allowed = true;
            } else {
                allowed = false;
            }

            return RateLimitResult.builder()
                    .allowed(allowed)
                    .current(deque.size())
                    .limit(limit)
                    .remainingTime(windowSeconds)
                    .build();
        }

        static class WindowCounter {
            long count;
            long expiresAt;
        }
    }

    /**
     * 信号量限流器
     */
    public static class SemaphoreRateLimiter {

        private final ConcurrentHashMap<String, Semaphore> semaphores = new ConcurrentHashMap<>();

        /**
         * 尝试获取许可
         *
         * @param key     限流键
         * @param permits 许可数量
         * @param timeout 超时时间
         * @param unit    时间单位
         * @return 是否获取成功
         */
        public boolean tryAcquire(String key, int permits, long timeout, TimeUnit unit) {
            Semaphore semaphore = semaphores.computeIfAbsent(key, k -> new Semaphore(permits));
            try {
                return semaphore.tryAcquire(timeout, unit);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        /**
         * 释放许可
         *
         * @param key 限流键
         */
        public void release(String key) {
            Semaphore semaphore = semaphores.get(key);
            if (semaphore != null) {
                semaphore.release();
            }
        }

        /**
         * 获取可用许可数
         *
         * @param key 限流键
         * @return 可用许可数
         */
        public int availablePermits(String key) {
            Semaphore semaphore = semaphores.get(key);
            return semaphore != null ? semaphore.availablePermits() : 0;
        }
    }

    /**
     * 限流结果
     */
    @Getter
    public static class RateLimitResult {
        private final boolean allowed;
        private final long current;
        private final long limit;
        private final long remainingTime;

        private RateLimitResult(boolean allowed, long current, long limit, long remainingTime) {
            this.allowed = allowed;
            this.current = current;
            this.limit = limit;
            this.remainingTime = remainingTime;
        }

        public static RateLimitResult allowed() {
            return new RateLimitResult(true, 0, 0, 0);
        }

        public static RateLimitResultBuilder builder() {
            return new RateLimitResultBuilder();
        }

        public static class RateLimitResultBuilder {
            private boolean allowed;
            private long current;
            private long limit;
            private long remainingTime;

            public RateLimitResultBuilder allowed(boolean allowed) {
                this.allowed = allowed;
                return this;
            }

            public RateLimitResultBuilder current(long current) {
                this.current = current;
                return this;
            }

            public RateLimitResultBuilder limit(long limit) {
                this.limit = limit;
                return this;
            }

            public RateLimitResultBuilder remainingTime(long remainingTime) {
                this.remainingTime = remainingTime;
                return this;
            }

            public RateLimitResult build() {
                return new RateLimitResult(allowed, current, limit, remainingTime);
            }
        }
    }
}
