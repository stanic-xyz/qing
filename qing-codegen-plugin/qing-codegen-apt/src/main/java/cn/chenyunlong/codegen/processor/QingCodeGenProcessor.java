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
import cn.hutool.core.map.MapUtil;

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
            processAnnotations(annotations, roundEnv);
            generateSourceFiles(roundEnv);
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

        annotations.forEach(annotation -> {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
            ElementFilter.typesIn(elements)
                .forEach(typeElement -> {
                    Elements elementUtils = processingEnv.getElementUtils();
                    HashSet<CodeGenProcessor> processorHashSet =
                        new HashSet<>(CodeGenContext.find(
                            Collections.singleton(annotation.getQualifiedName().toString())));

                    if (providers.containsKey(typeElement)) {
                        Set<CodeGenProcessor> codeGenProcessorList = providers.get(typeElement);
                        codeGenProcessorList.addAll(processorHashSet);
                    } else {
                        providers.put(typeElement, processorHashSet);
                    }
                    List<? extends AnnotationMirror> mirrors =
                        elementUtils.getAllAnnotationMirrors(typeElement);
                    Set<String> annotationNames = mirrors.stream()
                        .map(annotationMirror -> annotationMirror.getAnnotationType().toString())
                        .collect(Collectors.toSet());

                });
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
        codeGenProcessorList.stream()
            // 根据执行顺序来
            .sorted(Comparator.comparing(CodeGenProcessor::getOrder))
            .forEach(codeGenProcessor -> codeGenProcessor.generateClass(className, roundEnvironment,
                true));
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
