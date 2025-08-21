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

package cn.chenyunlong.codegen.processor;

import cn.chenyunlong.codegen.context.CodeGenContext;
import cn.chenyunlong.codegen.context.ProcessingEnvironmentHolder;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import cn.chenyunlong.codegen.cache.CacheManager;
import cn.chenyunlong.codegen.metrics.PerformanceMetrics;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import java.util.*;
import java.util.stream.Collectors;

import static cn.chenyunlong.codegen.context.ProcessingEnvironmentHolder.fatalError;
import static cn.chenyunlong.codegen.context.ProcessingEnvironmentHolder.log;
import static com.google.common.base.Throwables.getStackTraceAsString;

/**
 * 代码生成器注册器。
 *
 * @author gim
 * @since 2023-10-24
 */
@SupportedOptions({"debug", "verify", "baseDir"})
public class QingCodeGenProcessor extends AbstractProcessor {

    private final List<String> exceptionStacks = Collections.synchronizedList(new ArrayList<>());

    // 每一个类需要生成的类型
    private final Map<TypeElement, Set<CodeGenProcessor>> providers =
        MapUtil.newConcurrentHashMap();
    
    // 缓存已处理的类型，避免重复处理
    private final Set<String> processedTypes = Collections.synchronizedSet(new HashSet<>());
    
    // 缓存生成的文件，避免重复生成
    private final Set<String> generatedFiles = Collections.synchronizedSet(new HashSet<>());


    /**
     * 注解处理器主要处理逻辑。
     *
     * @param annotations 文档元素列表
     * @param roundEnv    当前编译环境（相当于上下文）
     * @return boolean
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        log("未配置编译器参数：baseDir，请通过编译器命令：-AbaseDir=/path 指定代码生成路径");
        try {
            return processImpl(annotations, roundEnv);
        } catch (RuntimeException exception) {
            // We don't allow exceptions to any kind to propagate to the compiler
            String stackTraceString = getStackTraceAsString(exception);
            exceptionStacks.add(stackTraceString);
            fatalError(stackTraceString);
        }
        return false;
    }

    private boolean processImpl(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!annotations.isEmpty()) {
            // 每轮处理开始时清空缓存，确保能正确处理所有注解
            if (!roundEnv.processingOver()) {
                // 非最后一轮，清空当前轮次的处理缓存
                providers.clear();
                log("清空providers缓存，开始新一轮处理");
                
                // 开始新的编译轮次
                PerformanceMetrics.startCompilationRound();
            }
            
            processAnnotations(annotations, roundEnv);
            generateSourceFiles(roundEnv);
        } else if (roundEnv.processingOver()) {
            // 最后一轮处理且没有注解，清理所有缓存
            processedTypes.clear();
            providers.clear();
            generatedFiles.clear();
            log("处理结束，清理所有缓存");
            
            // 打印性能报告
            PerformanceMetrics.printPerformanceReport();
        }
        return false;
    }

    /**
     * 生成文件。
     */
    private void generateSourceFiles(RoundEnvironment roundEnv) {
        providers.forEach(
            (className, genProcessorList) -> doGenerate(className, genProcessorList, roundEnv));
    }

    /**
     * 处理注解。
     *
     * @param annotations 注解列表
     * @param roundEnv    周围环境
     */
    private void processAnnotations(Set<? extends TypeElement> annotations,
                                    RoundEnvironment roundEnv) {

        log(annotations.toString());

        // 收集所有带注解的类型元素
        Set<TypeElement> allAnnotatedTypes = new HashSet<>();
        annotations.forEach(annotation -> {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
            allAnnotatedTypes.addAll(ElementFilter.typesIn(elements));
        });
        
        // 为每个类型元素收集所有相关的处理器
        allAnnotatedTypes.forEach(typeElement -> {
            String typeKey = typeElement.getQualifiedName().toString();
            // 注释掉processedTypes检查，允许每轮都重新处理
            // if (processedTypes.contains(typeKey)) {
            //     log(StrUtil.format("跳过已处理的类型：{}", typeKey));
            //     return;
            // }
            log(StrUtil.format("开始处理类型：{}", typeKey));
            
            Elements elementUtils = processingEnv.getElementUtils();
            List<? extends AnnotationMirror> mirrors =
                elementUtils.getAllAnnotationMirrors(typeElement);
            Set<String> annotationNames = mirrors.stream()
                .map(annotationMirror -> annotationMirror.getAnnotationType().toString())
                .collect(Collectors.toSet());
            
            log(StrUtil.format("类型 {} 的所有注解: {}", typeKey, annotationNames));

            // 为该类型收集所有相关的处理器
            Set<CodeGenProcessor> allProcessors = new HashSet<>();
            for (String annotationName : annotationNames) {
                Set<CodeGenProcessor> processors = CodeGenContext.find(Collections.singleton(annotationName));
                allProcessors.addAll(processors);
                log(StrUtil.format("为完整注解名 {} 找到 {} 个处理器", annotationName, processors.size()));
                
                // 如果没有找到处理器，尝试使用简单类名
                if (processors.isEmpty() && annotationName.contains(".")) {
                    String simpleAnnotationName = annotationName.substring(annotationName.lastIndexOf('.') + 1);
                    Set<CodeGenProcessor> simpleProcessors = CodeGenContext.find(Collections.singleton(simpleAnnotationName));
                    allProcessors.addAll(simpleProcessors);
                    log(StrUtil.format("使用简单类名 {} 找到 {} 个处理器", simpleAnnotationName, simpleProcessors.size()));
                }
            }
            
            log(StrUtil.format("类型 {} 总共收集到 {} 个处理器", typeKey, allProcessors.size()));
            
            if (!allProcessors.isEmpty()) {
                providers.put(typeElement, allProcessors);
                processedTypes.add(typeKey);
                log(StrUtil.format("为类型 {} 收集了 {} 个处理器", typeKey, allProcessors.size()));
            }
        });
    }

    /**
     * 执行具体的代码生成逻辑。
     *
     * @param className            元素名称
     * @param codeGenProcessorList 代码生成器列表
     * @param roundEnvironment     执行环境
     */
    private void doGenerate(TypeElement className, Set<CodeGenProcessor> codeGenProcessorList,
                            RoundEnvironment roundEnvironment) {
        String classKey = className.getQualifiedName().toString();
        
        codeGenProcessorList.stream()
            .sorted(Comparator.comparing(CodeGenProcessor::getOrder))
            .forEach(codeGenProcessor -> {
                String processorName = codeGenProcessor.getClass().getSimpleName();
                String fileKey = classKey + "_" + processorName;
                
                // 检查是否已经生成过该文件
                if (generatedFiles.contains(fileKey)) {
                    log(StrUtil.format("跳过已生成的文件：{}，处理器：{}", className, processorName));
                    return;
                }
                
                long startTime = System.currentTimeMillis();
                try {
                    // 检查处理器是否支持该类型
                    if (!codeGenProcessor.support(className, roundEnvironment)) {
                        log(StrUtil.format("处理器 {} 不支持类型：{}", processorName, className));
                        return;
                    }
                    
                    codeGenProcessor.generateClass(className, roundEnvironment, true);
                    generatedFiles.add(fileKey);
                    
                    long duration = System.currentTimeMillis() - startTime;
                    log(StrUtil.format("成功生成类：{}，处理器：{}，耗时：{}ms", className, processorName, duration));
                } catch (Exception e) {
                    long duration = System.currentTimeMillis() - startTime;
                    String errorMsg = StrUtil.format("生成类 {} 时发生异常，处理器：{}，耗时：{}ms，错误：{}", 
                        className, processorName, duration, e.getMessage());
                    log(errorMsg);
                    exceptionStacks.add(errorMsg + "\n" + getStackTraceAsString(e));
                }
            });
    }

    /**
     * 初始化。
     *
     * @param processingEnvironment 加工环境
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        ProcessingEnvironmentHolder.setProcessingEnvironment(processingEnvironment);
        log("》》》》》初始化代码生成器》》》》》");
        super.init(processingEnvironment);
        CodeGenContext.init(processingEnvironment);
        
        // 初始化缓存管理器
        String baseDir = processingEnvironment.getOptions().get("baseDir");
        if (baseDir != null) {
            CacheManager.initialize(baseDir);
            log("》》》》》缓存管理器初始化完毕》》》》》");
        } else {
            log("》》》》》未配置baseDir，跳过缓存管理器初始化》》》》》");
        }
        
        log("》》》》》初始化代码生成器完毕》》》》》");
    }

    /**
     * 获取支持的源版本。
     *
     * @return {@link SourceVersion}
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return CodeGenContext.getSupportedAnnotationTypes();
    }

    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }

}
