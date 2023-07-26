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

import cn.chenyunlong.codegen.context.CodeGenProcessorContext;
import cn.chenyunlong.codegen.context.ProcessingEnvironmentHolder;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gim
 * @since 2023-10-24
 */
public class QingCodeGenProcessorRegistry extends AbstractProcessor {

    /**
     * 过程
     *
     * @param elements         文档元素列表
     * @param roundEnvironment 当前编译环境（相当于上下文）
     * @return boolean
     */
    @Override
    public boolean process(Set<? extends TypeElement> elements, RoundEnvironment roundEnvironment) {
        if (elements.isEmpty()) {
            System.out.println("空注解不需要处理");
            return true;
        }
        try {
            System.out.println("正在处理注解：" + elements
                    .stream()
                    .map(TypeElement::getQualifiedName)
                    .collect(Collectors.joining(",")));

            final Set<Element> typeElements = new HashSet<>();
            elements.forEach(element -> typeElements.addAll(roundEnvironment.getElementsAnnotatedWith(element)));
            // 对每个对象进行生成操作
            // 加载需要处理的类的所有注解
            Collections.unmodifiableSet(ElementFilter.typesIn(typeElements)).forEach(typeElement -> {
                List<? extends AnnotationMirror> mirrors =
                        processingEnv.getElementUtils().getAllAnnotationMirrors(typeElement);
                Set<String> annotationNames = mirrors
                        .stream()
                        .map(annotationMirror -> annotationMirror.getAnnotationType().toString())
                        .collect(Collectors.toSet());
                List<CodeGenProcessor> codeGenProcessorList = CodeGenProcessorContext
                        // 根据注解类型生成代码
                        .find(annotationNames)
                        .stream()
                        .filter(codeGenProcessor -> codeGenProcessor.support(typeElement, roundEnvironment))
                        .toList();
                // 执行代码生成逻辑
                doGenerate(typeElement, codeGenProcessorList, roundEnvironment);
            });
        } catch (Exception exception) {
            exception.printStackTrace();
            processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, "CodeGen：代码生成器异常！");
        }
        // 只需要执行一次
        return true;
    }

    /**
     * 执行具体的代码生成逻辑
     *
     * @param typeElement          需要处理的元素信息
     * @param codeGenProcessorList 代码生成器列表
     * @param roundEnvironment     执行环境
     */
    private void doGenerate(TypeElement typeElement, List<CodeGenProcessor> codeGenProcessorList, RoundEnvironment roundEnvironment) {
        codeGenProcessorList.stream()
                            // 根据执行顺序来
                            .sorted(Comparator.comparing(CodeGenProcessor::getOrder))
                            .forEach(codeGenProcessor -> codeGenProcessor.generateClass(typeElement, roundEnvironment, true));
    }

    /**
     * 初始化
     *
     * @param processingEnvironment 加工环境
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        ProcessingEnvironmentHolder.printMessage("》》》》》初始化代码生成器》》》》》");
        super.init(processingEnvironment);
        ProcessingEnvironmentHolder.setProcessingEnvironment(processingEnvironment);
        CodeGenProcessorContext.init(processingEnvironment);
        ProcessingEnvironmentHolder.printMessage("》》》》》初始化代码生成器完毕》》》》》");
    }


    /**
     * 获取支持的源版本
     *
     * @return {@link SourceVersion}
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return CodeGenProcessorContext.getSupportedAnnotationTypes();
    }

    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }

}
