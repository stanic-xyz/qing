package cn.chenyunlong.qing.service.llm.config;

import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {

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
}
