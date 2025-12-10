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

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * <p>配置系统中使用的线程池，包括：</p>
 * <ul>
 *   <li>异步任务执行器：用于处理异步业务逻辑</li>
 *   <li>认证任务执行器：专门用于处理认证相关的异步任务</li>
 *   <li>通用任务执行器：用于处理一般性的异步任务</li>
 * </ul>
 *
 * <p>线程池参数说明：</p>
 * <ul>
 *   <li>corePoolSize：核心线程数，建议设置为CPU核心数</li>
 *   <li>maxPoolSize：最大线程数，建议设置为CPU核心数的2-4倍</li>
 *   <li>queueCapacity：队列容量，建议设置为核心线程数的2-4倍</li>
 *   <li>keepAliveSeconds：线程空闲时间，超过此时间的非核心线程将被回收</li>
 * </ul>
 *
 * @author 陈云龙
 * @since 1.0.0
 */
@Slf4j
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    /**
     * 获取CPU核心数
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * 异步任务执行器
     *
     * <p>用于处理一般性的异步任务，如：</p>
     * <ul>
     *   <li>发送邮件</li>
     *   <li>记录日志</li>
     *   <li>数据统计</li>
     * </ul>
     *
     * @return 异步任务执行器
     */
    @Bean("asyncTaskExecutor")
    public Executor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 核心线程数：CPU核心数
        executor.setCorePoolSize(CPU_COUNT);

        // 最大线程数：CPU核心数的2倍
        executor.setMaxPoolSize(CPU_COUNT * 2);

        // 队列容量：核心线程数的4倍
        executor.setQueueCapacity(CPU_COUNT * 4);

        // 线程空闲时间：60秒
        executor.setKeepAliveSeconds(60);

        // 线程名前缀
        executor.setThreadNamePrefix("async-task-");

        // 拒绝策略：由调用线程执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 等待所有任务完成后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);

        // 等待时间：30秒
        executor.setAwaitTerminationSeconds(30);

        // 初始化
        executor.initialize();

        log.info("异步任务执行器初始化完成 - 核心线程数: {}, 最大线程数: {}, 队列容量: {}",
                CPU_COUNT, CPU_COUNT * 2, CPU_COUNT * 4);

        return executor;
    }

    /**
     * 认证任务执行器
     *
     * <p>专门用于处理认证相关的异步任务，如：</p>
     * <ul>
     *   <li>用户登录记录</li>
     *   <li>认证失败记录</li>
     *   <li>令牌刷新</li>
     *   <li>安全事件记录</li>
     * </ul>
     *
     * @return 认证任务执行器
     */
    @Bean("authTaskExecutor")
    public Executor authTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 核心线程数：CPU核心数的一半，认证任务相对较少
        executor.setCorePoolSize(Math.max(1, CPU_COUNT / 2));

        // 最大线程数：CPU核心数
        executor.setMaxPoolSize(CPU_COUNT);

        // 队列容量：核心线程数的8倍，认证任务可能会有突发
        executor.setQueueCapacity(Math.max(1, CPU_COUNT / 2) * 8);

        // 线程空闲时间：120秒，认证任务执行频率相对较低
        executor.setKeepAliveSeconds(120);

        // 线程名前缀
        executor.setThreadNamePrefix("auth-task-");

        // 拒绝策略：由调用线程执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 等待所有任务完成后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);

        // 等待时间：60秒
        executor.setAwaitTerminationSeconds(60);

        // 初始化
        executor.initialize();

        log.info("认证任务执行器初始化完成 - 核心线程数: {}, 最大线程数: {}, 队列容量: {}",
                Math.max(1, CPU_COUNT / 2), CPU_COUNT, Math.max(1, CPU_COUNT / 2) * 8);

        return executor;
    }

    /**
     * 高优先级任务执行器
     *
     * <p>用于处理高优先级的异步任务，如：</p>
     * <ul>
     *   <li>安全告警</li>
     *   <li>系统监控</li>
     *   <li>紧急通知</li>
     * </ul>
     *
     * @return 高优先级任务执行器
     */
    @Bean("highPriorityTaskExecutor")
    public Executor highPriorityTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 核心线程数：较小，但保证有足够的线程处理高优先级任务
        executor.setCorePoolSize(2);

        // 最大线程数：CPU核心数的一半
        executor.setMaxPoolSize(Math.max(2, CPU_COUNT / 2));

        // 队列容量：较小，避免高优先级任务排队等待
        executor.setQueueCapacity(10);

        // 线程空闲时间：30秒，快速回收空闲线程
        executor.setKeepAliveSeconds(30);

        // 线程名前缀
        executor.setThreadNamePrefix("high-priority-");

        // 拒绝策略：抛出异常，确保高优先级任务不会被忽略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());

        // 等待所有任务完成后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);

        // 等待时间：15秒
        executor.setAwaitTerminationSeconds(15);

        // 初始化
        executor.initialize();

        log.info("高优先级任务执行器初始化完成 - 核心线程数: {}, 最大线程数: {}, 队列容量: {}",
                2, Math.max(2, CPU_COUNT / 2), 10);

        return executor;
    }

    /**
     * 线程池监控配置
     */
    @ConfigurationProperties(prefix = "qing.thread-pool")
    public static class ThreadPoolProperties {

        /**
         * 异步任务执行器配置
         */
        private AsyncTaskExecutorProperties asyncTask = new AsyncTaskExecutorProperties();

        /**
         * 认证任务执行器配置
         */
        private AuthTaskExecutorProperties authTask = new AuthTaskExecutorProperties();

        public AsyncTaskExecutorProperties getAsyncTask() {
            return asyncTask;
        }

        public void setAsyncTask(AsyncTaskExecutorProperties asyncTask) {
            this.asyncTask = asyncTask;
        }

        public AuthTaskExecutorProperties getAuthTask() {
            return authTask;
        }

        public void setAuthTask(AuthTaskExecutorProperties authTask) {
            this.authTask = authTask;
        }

        /**
         * 异步任务执行器属性
         */
        public static class AsyncTaskExecutorProperties {
            private int corePoolSize = CPU_COUNT;
            private int maxPoolSize = CPU_COUNT * 2;
            private int queueCapacity = CPU_COUNT * 4;
            private int keepAliveSeconds = 60;

            // getters and setters
            public int getCorePoolSize() {return corePoolSize;}

            public void setCorePoolSize(int corePoolSize) {this.corePoolSize = corePoolSize;}

            public int getMaxPoolSize() {return maxPoolSize;}

            public void setMaxPoolSize(int maxPoolSize) {this.maxPoolSize = maxPoolSize;}

            public int getQueueCapacity() {return queueCapacity;}

            public void setQueueCapacity(int queueCapacity) {this.queueCapacity = queueCapacity;}

            public int getKeepAliveSeconds() {return keepAliveSeconds;}

            public void setKeepAliveSeconds(int keepAliveSeconds) {this.keepAliveSeconds = keepAliveSeconds;}
        }

        /**
         * 认证任务执行器属性
         */
        public static class AuthTaskExecutorProperties {
            private int corePoolSize = Math.max(1, CPU_COUNT / 2);
            private int maxPoolSize = CPU_COUNT;
            private int queueCapacity = Math.max(1, CPU_COUNT / 2) * 8;
            private int keepAliveSeconds = 120;

            // getters and setters
            public int getCorePoolSize() {return corePoolSize;}

            public void setCorePoolSize(int corePoolSize) {this.corePoolSize = corePoolSize;}

            public int getMaxPoolSize() {return maxPoolSize;}

            public void setMaxPoolSize(int maxPoolSize) {this.maxPoolSize = maxPoolSize;}

            public int getQueueCapacity() {return queueCapacity;}

            public void setQueueCapacity(int queueCapacity) {this.queueCapacity = queueCapacity;}

            public int getKeepAliveSeconds() {return keepAliveSeconds;}

            public void setKeepAliveSeconds(int keepAliveSeconds) {this.keepAliveSeconds = keepAliveSeconds;}
        }
    }
}
