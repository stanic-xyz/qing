/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.codegen.metrics;

import cn.chenyunlong.codegen.context.ProcessingEnvironmentHolder;
import cn.hutool.core.util.StrUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 代码生成器性能指标记录器。
 * 用于监控代码生成过程中的性能数据，包括生成文件数量、用时、缓存命中率等。
 *
 * @author gim
 * @since 2023-12-20
 */
public class PerformanceMetrics {

    // 总体统计
    private static final AtomicInteger totalFilesGenerated = new AtomicInteger(0);
    private static final AtomicInteger totalFilesSkipped = new AtomicInteger(0);
    private static final AtomicLong totalGenerationTime = new AtomicLong(0);
    private static final AtomicLong totalCacheCheckTime = new AtomicLong(0);
    
    // 按处理器分类统计
    private static final ConcurrentHashMap<String, ProcessorMetrics> processorMetrics = new ConcurrentHashMap<>();
    
    // 缓存统计
    private static final AtomicInteger cacheHits = new AtomicInteger(0);
    private static final AtomicInteger cacheMisses = new AtomicInteger(0);
    
    // 编译轮次统计
    private static final AtomicInteger compilationRounds = new AtomicInteger(0);
    private static long compilationStartTime = 0;
    
    /**
     * 处理器性能指标。
     */
    public static class ProcessorMetrics {
        private final AtomicInteger filesGenerated = new AtomicInteger(0);
        private final AtomicInteger filesSkipped = new AtomicInteger(0);
        private final AtomicLong generationTime = new AtomicLong(0);
        private final AtomicLong cacheCheckTime = new AtomicLong(0);
        
        public int getFilesGenerated() { return filesGenerated.get(); }
        public int getFilesSkipped() { return filesSkipped.get(); }
        public long getGenerationTime() { return generationTime.get(); }
        public long getCacheCheckTime() { return cacheCheckTime.get(); }
        
        public void incrementFilesGenerated() { filesGenerated.incrementAndGet(); }
        public void incrementFilesSkipped() { filesSkipped.incrementAndGet(); }
        public void addGenerationTime(long time) { generationTime.addAndGet(time); }
        public void addCacheCheckTime(long time) { cacheCheckTime.addAndGet(time); }
    }
    
    /**
     * 开始编译轮次。
     */
    public static void startCompilationRound() {
        if (compilationStartTime == 0) {
            compilationStartTime = System.currentTimeMillis();
        }
        compilationRounds.incrementAndGet();
        ProcessingEnvironmentHolder.log(StrUtil.format(
            "[性能监控] 开始第 {} 轮编译", compilationRounds.get()));
    }
    
    /**
     * 记录文件生成。
     *
     * @param processorName 处理器名称
     * @param fileName 文件名
     * @param generationTime 生成用时（毫秒）
     */
    public static void recordFileGenerated(String processorName, String fileName, long generationTime) {
        totalFilesGenerated.incrementAndGet();
        totalGenerationTime.addAndGet(generationTime);
        
        ProcessorMetrics metrics = processorMetrics.computeIfAbsent(processorName, k -> new ProcessorMetrics());
        metrics.incrementFilesGenerated();
        metrics.addGenerationTime(generationTime);
        
        ProcessingEnvironmentHolder.log(StrUtil.format(
            "[性能监控] [{}] 生成文件: {} (用时: {}ms)", processorName, fileName, generationTime));
    }
    
    /**
     * 记录文件跳过。
     *
     * @param processorName 处理器名称
     * @param fileName 文件名
     * @param reason 跳过原因
     */
    public static void recordFileSkipped(String processorName, String fileName, String reason) {
        totalFilesSkipped.incrementAndGet();
        
        ProcessorMetrics metrics = processorMetrics.computeIfAbsent(processorName, k -> new ProcessorMetrics());
        metrics.incrementFilesSkipped();
        
        ProcessingEnvironmentHolder.log(StrUtil.format(
            "[性能监控] [{}] 跳过文件: {} (原因: {})", processorName, fileName, reason));
    }
    
    /**
     * 记录缓存检查时间。
     *
     * @param processorName 处理器名称
     * @param cacheCheckTime 缓存检查用时（毫秒）
     * @param hit 是否命中缓存
     */
    public static void recordCacheCheck(String processorName, long cacheCheckTime, boolean hit) {
        totalCacheCheckTime.addAndGet(cacheCheckTime);
        
        ProcessorMetrics metrics = processorMetrics.computeIfAbsent(processorName, k -> new ProcessorMetrics());
        metrics.addCacheCheckTime(cacheCheckTime);
        
        if (hit) {
            cacheHits.incrementAndGet();
        } else {
            cacheMisses.incrementAndGet();
        }
    }
    
    /**
     * 打印性能报告。
     */
    public static void printPerformanceReport() {
        long totalTime = System.currentTimeMillis() - compilationStartTime;
        int totalFiles = totalFilesGenerated.get() + totalFilesSkipped.get();
        
        ProcessingEnvironmentHolder.log("\n" + "=".repeat(80));
        ProcessingEnvironmentHolder.log("[性能监控] 代码生成器性能报告");
        ProcessingEnvironmentHolder.log("=".repeat(80));
        
        // 总体统计
        ProcessingEnvironmentHolder.log(StrUtil.format(
            "总编译时间: {}ms | 编译轮次: {} | 处理文件总数: {}", 
            totalTime, compilationRounds.get(), totalFiles));
        
        ProcessingEnvironmentHolder.log(StrUtil.format(
            "生成文件: {} | 跳过文件: {} | 生成用时: {}ms", 
            totalFilesGenerated.get(), totalFilesSkipped.get(), totalGenerationTime.get()));
        
        // 缓存统计
        int totalCacheChecks = cacheHits.get() + cacheMisses.get();
        double cacheHitRate = totalCacheChecks > 0 ? (double) cacheHits.get() / totalCacheChecks * 100 : 0;
        ProcessingEnvironmentHolder.log(StrUtil.format(
            "缓存命中: {} | 缓存未命中: {} | 命中率: {:.1f}% | 缓存检查用时: {}ms", 
            cacheHits.get(), cacheMisses.get(), cacheHitRate, totalCacheCheckTime.get()));
        
        // 性能指标
        if (totalFiles > 0) {
            double avgTimePerFile = (double) totalGenerationTime.get() / totalFilesGenerated.get();
            ProcessingEnvironmentHolder.log(StrUtil.format(
                "平均生成时间: {:.2f}ms/文件 | 生成效率: {:.1f}文件/秒", 
                avgTimePerFile, totalFiles * 1000.0 / totalTime));
        }
        
        // 按处理器统计
        ProcessingEnvironmentHolder.log("\n按处理器统计:");
        ProcessingEnvironmentHolder.log("-".repeat(80));
        processorMetrics.forEach((processorName, metrics) -> {
            int processorTotal = metrics.getFilesGenerated() + metrics.getFilesSkipped();
            double processorAvgTime = metrics.getFilesGenerated() > 0 ? 
                (double) metrics.getGenerationTime() / metrics.getFilesGenerated() : 0;
            
            ProcessingEnvironmentHolder.log(StrUtil.format(
                "[{}] 总数: {} | 生成: {} | 跳过: {} | 平均用时: {:.2f}ms | 缓存检查: {}ms", 
                processorName, processorTotal, metrics.getFilesGenerated(), 
                metrics.getFilesSkipped(), processorAvgTime, metrics.getCacheCheckTime()));
        });
        
        ProcessingEnvironmentHolder.log("=".repeat(80));
    }
    
    /**
     * 重置所有指标。
     */
    public static void reset() {
        totalFilesGenerated.set(0);
        totalFilesSkipped.set(0);
        totalGenerationTime.set(0);
        totalCacheCheckTime.set(0);
        cacheHits.set(0);
        cacheMisses.set(0);
        compilationRounds.set(0);
        compilationStartTime = 0;
        processorMetrics.clear();
    }
    
    /**
     * 获取总生成文件数。
     */
    public static int getTotalFilesGenerated() {
        return totalFilesGenerated.get();
    }
    
    /**
     * 获取总跳过文件数。
     */
    public static int getTotalFilesSkipped() {
        return totalFilesSkipped.get();
    }
    
    /**
     * 获取缓存命中率。
     */
    public static double getCacheHitRate() {
        int totalChecks = cacheHits.get() + cacheMisses.get();
        return totalChecks > 0 ? (double) cacheHits.get() / totalChecks * 100 : 0;
    }
}