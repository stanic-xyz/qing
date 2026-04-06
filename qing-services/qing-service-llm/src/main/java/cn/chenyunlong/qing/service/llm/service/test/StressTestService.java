package cn.chenyunlong.qing.service.llm.service.test;

import cn.chenyunlong.qing.service.llm.config.BusinessThreadPoolManager;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

@Service
public class StressTestService {
    @Resource
    private BusinessThreadPoolManager poolManager;

    private volatile boolean running = false;
    private StressTestContext currentContext;

    // 启动压力测试（基于总请求数 + 并发线程数）
    public void startTest(int concurrency, int totalRequests, int simulatedDelayMs) {
        if (running) {
            throw new IllegalStateException("Another stress test is already running");
        }
        this.running = true;
        this.currentContext = new StressTestContext(concurrency, totalRequests, simulatedDelayMs);
        // 异步执行测试
        new Thread(this::executeTest).start();
    }

    private void executeTest() {
        final StressTestContext ctx = currentContext;
        final int concurrency = ctx.concurrency;
        final int totalRequests = ctx.totalRequests;
        final int delayMs = ctx.simulatedDelayMs;

        // 统计指标
        LongAdder successCount = new LongAdder();
        LongAdder failCount = new LongAdder();
        // 记录所有响应时间（用于计算 TP99，这里简化：只记录平均值和最大值）
        AtomicLong totalResponseTime = new AtomicLong(0);
        AtomicLong maxResponseTime = new AtomicLong(0);

        // 使用 CountDownLatch 等待所有请求完成
        CountDownLatch latch = new CountDownLatch(totalRequests);
        // 使用 Semaphore 控制并发数
        Semaphore semaphore = new Semaphore(concurrency);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < totalRequests && running; i++) {
            try {
                semaphore.acquire();
                poolManager.submit(() -> {
                    try {
                        long taskStart = System.nanoTime();
                        // 模拟业务处理（可替换为真实调用）
                        System.out.println("taskStart = " + taskStart);
                        if (delayMs > 0) Thread.sleep(delayMs);
                        long duration = System.nanoTime() - taskStart;
                        long durationMs = duration / 1_000_000;
                        totalResponseTime.addAndGet(durationMs);
                        maxResponseTime.updateAndGet(cur -> Math.max(cur, durationMs));
                        successCount.increment();
                    } catch (Exception e) {
                        failCount.increment();
                    } finally {
                        semaphore.release();
                        latch.countDown();
                    }
                    return null;
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        try {
            latch.await(); // 等待所有请求完成
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long endTime = System.currentTimeMillis();
        long totalDuration = endTime - startTime;
        long successes = successCount.sum();
        long failures = failCount.sum();
        long total = successes + failures;
        double qps = (totalDuration > 0) ? (total * 1000.0 / totalDuration) : 0;
        double avgRt = (successes > 0) ? (totalResponseTime.get() / (double) successes) : 0;
        long maxRt = maxResponseTime.get();

        ctx.result = new StressTestResult(total, successes, failures, totalDuration, qps, avgRt, maxRt);
        running = false;
    }

    public void stopTest() {
        running = false;
    }

    public StressTestResult getCurrentResult() {
        return currentContext != null ? currentContext.result : null;
    }

    public boolean isRunning() {
        return running;
    }

    // 内部上下文
    private static class StressTestContext {
        final int concurrency;
        final int totalRequests;
        final int simulatedDelayMs;
        volatile StressTestResult result;

        StressTestContext(int concurrency, int totalRequests, int simulatedDelayMs) {
            this.concurrency = concurrency;
            this.totalRequests = totalRequests;
            this.simulatedDelayMs = simulatedDelayMs;
        }
    }

    public static class StressTestResult {
        public final long totalRequests, successCount, failCount;
        public final long durationMs;
        public final double qps;
        public final double avgResponseTimeMs;
        public final long maxResponseTimeMs;

        public StressTestResult(long total, long success, long fail, long duration,
                                double qps, double avgRt, long maxRt) {
            this.totalRequests = total;
            this.successCount = success;
            this.failCount = fail;
            this.durationMs = duration;
            this.qps = qps;
            this.avgResponseTimeMs = avgRt;
            this.maxResponseTimeMs = maxRt;
        }
    }
}
