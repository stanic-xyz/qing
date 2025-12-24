/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.codegen.context;

import cn.chenyunlong.codegen.constraint.CondeGenConstant;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import cn.chenyunlong.codegen.util.StringUtils;

import javax.annotation.processing.ProcessingEnvironment;
import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static cn.chenyunlong.codegen.context.ProcessingEnvironmentHolder.log;

/**
 * 通过SPI 加载所有的CodeGenProcessor 识别要处理的annotation标记类。
 *
 * @author 陈云龙
 * @since 2022/11/28
 */
public final class CodeGenContext {

    private static final Map<String, CodeGenProcessor> PROCESSOR_MAP = new LinkedHashMap<>();
    private static final Map<Set<String>, Set<CodeGenProcessor>> PROCESSOR_CACHE = new LinkedHashMap<>();
    private static NameContext nameContext;
    private static File baseDirFile = null;
    private static volatile boolean initialized = false;

    private CodeGenContext() {
        throw new UnsupportedOperationException();
    }

    /**
     * 查询通过spi配置进来的代码生成处理器。
     *
     * @param annotationClassName 注释类名
     * @return {@link CodeGenProcessor}
     */
    public static Set<CodeGenProcessor> find(Set<String> annotationClassName) {
        // 使用缓存提高性能
        if (PROCESSOR_CACHE.containsKey(annotationClassName)) {
            return PROCESSOR_CACHE.get(annotationClassName);
        }
        
        Set<CodeGenProcessor> result = PROCESSOR_MAP
            .entrySet()
            .stream()
            .filter(stringCodeGenProcessorEntry -> annotationClassName.contains(
                stringCodeGenProcessorEntry.getKey()))
            .map(Map.Entry::getValue)
            .collect(Collectors.toSet());
            
        // 缓存结果
        PROCESSOR_CACHE.put(new HashSet<>(annotationClassName), result);
        
        return result;
    }

    /**
     * 初始化处理器。
     * spi 加载所有的processor
     */
    public static synchronized void init(ProcessingEnvironment processingEnvironment) {
        // 避免重复初始化
        if (initialized) {
            log("代码生成器已经初始化，跳过重复初始化");
            return;
        }
        
        try {
            nameContext = new NameContext();
            nameContext.init();
            
            ServiceLoader<CodeGenProcessor> codeGenProcessors =
                ServiceLoader.load(CodeGenProcessor.class, CodeGenProcessor.class.getClassLoader());
                
            int processorCount = 0;
            for (CodeGenProcessor codeGenProcessor : codeGenProcessors) {
                try {
                    ProcessingEnvironmentHolder.printMessage(
                        "[codegen-plugin]：加载处理器：%s".formatted(codeGenProcessor
                            .getClass()
                            .getName()));
                    codeGenProcessor.init(processingEnvironment);
                    PROCESSOR_MAP.put(codeGenProcessor.getSupportedAnnotation().getName(),
                        codeGenProcessor);
                    processorCount++;
                } catch (Exception e) {
                    ProcessingEnvironmentHolder.printMessage(
                        "[codegen-plugin]：加载处理器失败：%s，错误：%s".formatted(
                            codeGenProcessor.getClass().getName(), e.getMessage()));
                }
            }
            
            log(String.format("成功加载 %d 个代码生成处理器", processorCount));
            
            String baseDir =
                processingEnvironment.getOptions().get(CondeGenConstant.SOURCE_PATH_ARG_NAME);
            if (StringUtils.isNotBlank(baseDir)) {
                log(String.format("编译选项：baseDir：%s", baseDir));
                File file = Paths.get(baseDir).toFile();
                if (file.exists()) {
                    baseDirFile = file;
                    log(String.format("设置代码生成基础目录：%s", file.getAbsolutePath()));
                } else {
                    log(String.format("警告：指定的baseDir不存在：%s", baseDir));
                }
            } else {
                log("警告：未指定baseDir参数，将使用默认路径");
            }
            
            initialized = true;
            log("代码生成器初始化完成");
            
        } catch (Exception e) {
            log("代码生成器初始化失败：" + e.getMessage());
            throw new RuntimeException("代码生成器初始化失败", e);
        }
    }

    public static Set<String> getSupportedAnnotationTypes() {
        return PROCESSOR_MAP.keySet();
    }

    /**
     * 获取文件生成根路径
     *
     * @return 文件生成根路径
     */
    public static File getBaseDir() {
        return baseDirFile == null ? null : new File(baseDirFile, "src/main/java");
    }
}
