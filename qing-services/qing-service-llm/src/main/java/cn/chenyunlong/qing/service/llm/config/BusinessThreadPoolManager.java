package cn.chenyunlong.qing.service.llm.config;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class BusinessThreadPoolManager {

    private ThreadPoolExecutor executor;

    private final int defaultCoreSize = 10;
    private final int defaultMaxSize = 50;
    private final int defaultQueueCapacity = 100;

    public BusinessThreadPoolManager() {
        this.executor = createExecutor(defaultCoreSize, defaultMaxSize, defaultQueueCapacity);
    }

    private ThreadPoolExecutor createExecutor(int coreSize, int maxSize, int queueCapacity) {
        return new ThreadPoolExecutor(
                coreSize, maxSize,
                60L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueCapacity),
                new CustomThreadFactory("llm-business"),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    // 提交任务
    public <T> Future<T> submit(Callable<T> task) {
        return executor.submit(task);
    }

    // 动态调整线程池参数
    public synchronized void updatePoolConfig(int corePoolSize, int maxPoolSize, int queueCapacity) {
        if (corePoolSize > maxPoolSize) {
            throw new IllegalArgumentException("corePoolSize must <= maxPoolSize");
        }
        // 调整核心线程数
        executor.setCorePoolSize(corePoolSize);
        executor.setMaximumPoolSize(maxPoolSize);
        // 调整队列容量（需要重新创建队列，简单起见：如果新容量大于当前队列剩余容量，则拒绝；这里直接替换有风险，生产环境需谨慎）
        // 为了演示，只调整线程数，队列容量动态调整复杂，此处忽略，可改为支持修改
        // 实际可借助 setCorePoolSize + 预启动核心线程
    }

    // 获取线程池指标
    public ThreadPoolMetrics getMetrics() {
        return new ThreadPoolMetrics(
                executor.getCorePoolSize(),
                executor.getMaximumPoolSize(),
                executor.getActiveCount(),
                executor.getPoolSize(),
                executor.getQueue().size(),
                executor.getTaskCount(),
                executor.getCompletedTaskCount()
        );
    }

    @PreDestroy
    public void shutdown() {
        executor.shutdown();
    }

    static class CustomThreadFactory implements ThreadFactory {
        private final String namePrefix;
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        CustomThreadFactory(String namePrefix) {this.namePrefix = namePrefix;}

        public Thread newThread(@NonNull Runnable runnable) {
            Thread thread = new Thread(runnable, namePrefix + "-thread-" + threadNumber.getAndIncrement());
            thread.setUncaughtExceptionHandler((t, e) -> log.error("Thread {} threw an exception", t.getName(), e));
            return thread;
        }


    }

    public record ThreadPoolMetrics(int coreSize, int maxSize, int activeCount, int poolSize, int queueSize, long totalTasks, long completedTasks) {
    }
}
