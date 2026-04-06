package cn.chenyunlong.qing.service.llm.controler;

import cn.chenyunlong.qing.service.llm.config.BusinessThreadPoolManager;
import cn.chenyunlong.qing.service.llm.service.test.StressTestService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xdgf.usermodel.section.geometry.RelEllipticalArcTo;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@RestController
@RequestMapping("/test")
public class StressTestController {

    @Resource
    private StressTestService stressTestService;

    @Resource
    private BusinessThreadPoolManager poolManager;

    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    private final ReentrantLock lock = new ReentrantLock();

    @Resource
    private ExecutorService virtualTaskExecutor;

    // 启动压力测试
    @PostMapping("/start")
    public Map<String, String> startTest(@RequestBody StartRequest request) {
        if (stressTestService.isRunning()) {
            throw new IllegalStateException("Test already running, please stop first");
        }
        stressTestService.startTest(request.concurrency, request.totalRequests, request.simulatedDelayMs);
        Map<String, String> resp = new HashMap<>();
        resp.put("status", "started");
        resp.put("message", String.format("并发=%d, 总请求=%d, 模拟延时=%dms",
                request.concurrency, request.totalRequests, request.simulatedDelayMs));
        return resp;
    }

    // 停止压力测试
    @PostMapping("/stop")
    public Map<String, String> stopTest() {
        stressTestService.stopTest();
        Map<String, String> resp = new HashMap<>();
        resp.put("status", "stopped");
        return resp;
    }

    @PostMapping("/submitExceptionTask")
    public Map<String, String> submitExceptionTask() {
        CompletableFuture<Object>[] futures = new CompletableFuture[10];
        for (int i = 0; i < 10; i++) {
            futures[i] = CompletableFuture.supplyAsync(() -> {
                log.info("Task executed by submit taskExecutor, 当前线程：{}", Thread.currentThread().getName());
                throw new RuntimeException("Exception task for testing");
            }, taskExecutor);
        }

        // 1. 等待所有任务完成（忽略 allOf 自身的异常）
        CompletableFuture.allOf(futures)
                .exceptionally(ex -> null)   // 吞掉异常，使 allOf 正常完成
                .join();                     // 阻塞直到所有任务都完成

        // 2. 所有任务都已结束，统一处理结果或异常
        for (CompletableFuture<Object> future : futures) {
            try {
                Object result = future.join(); // 如果任务正常，返回结果；如果异常，抛出 CompletionException
                // 处理正常结果
                log.info("Task success: {}", result);
            } catch (Exception e) {
                // 处理异常
                log.error("Task failed", e);
            }
        }

        Map<String, String> resp = new HashMap<>();
        resp.put("status", "stopped");
        return resp;
    }


    @PostMapping("/executeExceptionTask")
    public Map<String, String> executeExceptionTask() {
        for (int i = 0; i < 10; i++) {
            taskExecutor.execute(() -> {
                // 没有内部处理线程内的异常，而是通过 UncaughtExceptionHandler 捕获之后的线程，会重新创建新的线程来执行，旧的线程直接废了
                log.info("Task executed by taskExecutor,当前线程：{}", Thread.currentThread().getName());
                throw new RuntimeException("Exception task for testing");
            });
        }
        Map<String, String> resp = new HashMap<>();
        resp.put("status", "stopped");
        return resp;
    }

    @PostMapping("/virtualTaskException")
    public Map<String, String> virtualTaskException() {
        for (int i = 0; i < 10000; i++) {
            virtualTaskExecutor.execute(() -> {
                try {
                    lock.wait(1000);
                    // 没有内部处理线程内的异常，而是通过 UncaughtExceptionHandler 捕获之后的线程，会重新创建新的线程来执行，旧的线程直接废了
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
                throw new RuntimeException("Exception task for testing");
            });
        }
        lock.notifyAll();
        Map<String, String> resp = new HashMap<>();
        resp.put("status", "stopped");
        return resp;
    }

    // 查询测试指标
    @GetMapping("/status")
    public Map<String, Object> getTestStatus() {
        Map<String, Object> info = new HashMap<>();
        info.put("running", stressTestService.isRunning());
        StressTestService.StressTestResult result = stressTestService.getCurrentResult();
        if (result != null) {
            info.put("result", result);
        }
        return info;
    }

    // 动态调整业务线程池配置
    @PostMapping("/threadpool/config")
    public Map<String, String> updateThreadPool(@RequestBody PoolConfigRequest request) {
        poolManager.updatePoolConfig(request.corePoolSize, request.maxPoolSize, request.queueCapacity);
        Map<String, String> resp = new HashMap<>();
        resp.put("status", "updated");
        resp.put("config", String.format("core=%d, max=%d, queue=%d",
                request.corePoolSize, request.maxPoolSize, request.queueCapacity));
        return resp;
    }

    // 获取线程池实时指标
    @GetMapping("/threadpool/metrics")
    public BusinessThreadPoolManager.ThreadPoolMetrics getThreadPoolMetrics() {
        return poolManager.getMetrics();
    }

    // ---------- 请求 DTO ----------
    static class StartRequest {
        public int concurrency;       // 压力测试并发线程数
        public int totalRequests;     // 总请求数
        public int simulatedDelayMs;  // 每个任务模拟的耗时（ms），模拟业务处理
    }

    static class PoolConfigRequest {
        public int corePoolSize;
        public int maxPoolSize;
        public int queueCapacity;
    }
}
