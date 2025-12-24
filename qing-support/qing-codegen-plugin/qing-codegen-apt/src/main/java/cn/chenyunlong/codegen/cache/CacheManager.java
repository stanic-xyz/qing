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

package cn.chenyunlong.codegen.cache;

import cn.chenyunlong.codegen.context.ProcessingEnvironmentHolder;
import cn.hutool.core.util.StrUtil;

import javax.lang.model.element.TypeElement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 缓存管理器。
 * 负责管理类结构哈希缓存，支持内存缓存和持久化缓存。
 * 优化编译性能，避免不必要的文件重新生成。
 *
 * @author 陈云龙
 * @since 2024-01-20
 */
public class CacheManager {

    private static final String CACHE_FILE_NAME = ".qing-codegen-cache";
    private static final String CACHE_VERSION = "1.0";

    // 内存缓存：类名 -> 结构哈希
    private static final ConcurrentMap<String, String> structureHashCache = new ConcurrentHashMap<>();

    // 内存缓存：文件路径 -> 生成状态
    private static final ConcurrentMap<String, Boolean> generatedFileCache = new ConcurrentHashMap<>();

    private static volatile boolean initialized = false;
    private static String cacheFilePath;

    /**
     * 初始化缓存管理器。
     *
     * @param baseDir 基础目录
     */
    public static synchronized void initialize(String baseDir) {
        if (initialized) {
            return;
        }

        if (StrUtil.isNotBlank(baseDir)) {
            cacheFilePath = Paths.get(baseDir, CACHE_FILE_NAME).toString();
            loadCacheFromFile();
        }

        initialized = true;
        ProcessingEnvironmentHolder.log("缓存管理器初始化完成，缓存文件路径: " + cacheFilePath);
    }

    /**
     * 检查类结构是否发生变化。
     *
     * @param typeElement 类型元素
     * @return true表示结构发生变化，需要重新生成
     */
    public static boolean isStructureChanged(TypeElement typeElement) {
        String className = typeElement.getQualifiedName().toString();
        String currentHash = ClassStructureAnalyzer.calculateStructureHash(typeElement);
        String cachedHash = structureHashCache.get(className);

        boolean changed = !ClassStructureAnalyzer.isSameStructure(currentHash, cachedHash);

        if (changed) {
            ProcessingEnvironmentHolder.log(StrUtil.format(
                "类结构发生变化: {}, 旧哈希: {}, 新哈希: {}",
                className, cachedHash, currentHash));
            // 更新缓存
            structureHashCache.put(className, currentHash);
        } else {
            ProcessingEnvironmentHolder.log(StrUtil.format(
                "类结构未变化: {}, 哈希: {}", className, currentHash));
        }

        return changed;
    }

    /**
     * 标记文件已生成。
     *
     * @param filePath 文件路径
     */
    public static void markFileGenerated(String filePath) {
        generatedFileCache.put(filePath, true);
        ProcessingEnvironmentHolder.log("标记文件已生成: " + filePath);
    }

    /**
     * 检查文件是否已生成。
     *
     * @param filePath 文件路径
     * @return 是否已生成
     */
    public static boolean isFileGenerated(String filePath) {
        return generatedFileCache.getOrDefault(filePath, false);
    }

    /**
     * 清理缓存。
     */
    public static void clearCache() {
        structureHashCache.clear();
        generatedFileCache.clear();
        ProcessingEnvironmentHolder.log("缓存已清理");
    }

    /**
     * 保存缓存到文件。
     */
    public static void saveCacheToFile() {
        if (StrUtil.isBlank(cacheFilePath)) {
            return;
        }

        try {
            Properties props = new Properties();
            props.setProperty("version", CACHE_VERSION);
            props.setProperty("timestamp", String.valueOf(System.currentTimeMillis()));

            // 保存结构哈希
            structureHashCache.forEach((className, hash) ->
                props.setProperty("structure." + className, hash));

            // 确保目录存在
            Path cacheFile = Paths.get(cacheFilePath);
            Files.createDirectories(cacheFile.getParent());

            try (FileOutputStream fos = new FileOutputStream(cacheFilePath)) {
                props.store(fos, "Qing CodeGen Cache - Generated at " + new java.util.Date());
            }

            ProcessingEnvironmentHolder.log("缓存已保存到文件: " + cacheFilePath);

        } catch (IOException e) {
            ProcessingEnvironmentHolder.log("保存缓存文件失败: " + e.getMessage());
        }
    }

    /**
     * 从文件加载缓存。
     */
    private static void loadCacheFromFile() {
        if (StrUtil.isBlank(cacheFilePath)) {
            return;
        }

        File cacheFile = new File(cacheFilePath);
        if (!cacheFile.exists()) {
            ProcessingEnvironmentHolder.log("缓存文件不存在，将创建新的缓存: " + cacheFilePath);
            return;
        }

        try {
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream(cacheFile)) {
                props.load(fis);
            }

            String version = props.getProperty("version");
            if (!CACHE_VERSION.equals(version)) {
                ProcessingEnvironmentHolder.log("缓存版本不匹配，清理旧缓存: " + version + " -> " + CACHE_VERSION);
                return;
            }

            // 加载结构哈希
            props.forEach((key, value) -> {
                String keyStr = key.toString();
                if (keyStr.startsWith("structure.")) {
                    String className = keyStr.substring("structure.".length());
                    structureHashCache.put(className, value.toString());
                }
            });

            ProcessingEnvironmentHolder.log(StrUtil.format(
                "从缓存文件加载了 {} 个类结构哈希", structureHashCache.size()));

        } catch (IOException e) {
            ProcessingEnvironmentHolder.log("加载缓存文件失败: " + e.getMessage());
        }
    }

    /**
     * 获取缓存统计信息。
     *
     * @return 缓存统计信息
     */
    public static String getCacheStats() {
        return StrUtil.format(
            "缓存统计 - 结构哈希: {}, 生成文件: {}",
            structureHashCache.size(),
            generatedFileCache.size());
    }
}
