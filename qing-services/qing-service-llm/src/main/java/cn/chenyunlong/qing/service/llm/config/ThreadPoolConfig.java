package cn.chenyunlong.qing.service.llm.config;

import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfig {

    @Value("${spring.datasource.hikari.maximum-pool-size:10}")
    private int dbMaxPoolSize;

    @Bean
    public ThreadPoolExecutor llmTaskExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 100, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100));
        executor.allowCoreThreadTimeOut(true);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.setCorePoolSize(2);
        executor.setMaximumPoolSize(2);
        executor.setThreadFactory(new BusinessThreadPoolManager.CustomThreadFactory("llm-task"));
        return executor;
    }

    /**
     * IO密集型任务线程池
     *
     * @return ThreadPoolTaskExecutor
     */
    @Bean
    public ExecutorService virtualTaskExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }


    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("qing-task-");
        executor.initialize();
        return executor;
    }

    @Bean("matchThreadPool")
    public Executor matchThreadPool() {
        // 核心线程数建议 = 连接池最大连接数 * 0.8 ~ 1
        // 防止线程超过连接数导致大量等待
        int coreSize = Math.max(4, (int) (dbMaxPoolSize * 0.8));
        int maxSize = dbMaxPoolSize;  // 最大不超过连接池大小
        long keepAlive = 60L;
        int queueCapacity = 100;  // 有界队列，防止堆积过多

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                coreSize, maxSize, keepAlive, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueCapacity),
                new ThreadPoolExecutor.CallerRunsPolicy()  // 队列满时让调用线程执行，提供反压
        );
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }
}
