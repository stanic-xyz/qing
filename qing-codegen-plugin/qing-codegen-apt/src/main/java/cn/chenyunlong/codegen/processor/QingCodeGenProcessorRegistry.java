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

import cn.chenyunlong.codegen.context.ProcessingEnvironmentHolder;
import cn.chenyunlong.codegen.registry.CodeGenProcessorRegistry;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * @author gim
 * @since 2023-10-24
 */
@SupportedOptions({})
@SupportedAnnotationTypes({})
public class QingCodeGenProcessorRegistry extends CodeGenProcessor {

    /**
     * 过程
     *
     * @param elements    文档元素列表
     * @param environment 周围环境
     * @return boolean
     */
    @Override
    public boolean process(Set<? extends TypeElement> elements, RoundEnvironment environment) {
        try {
            elements.forEach(element ->
            {
                Set<? extends Element> typeElements = environment.getElementsAnnotatedWith(element);
                // 加载需要处理的类的所有注解
                Collections.unmodifiableSet(ElementFilter.typesIn(typeElements)).forEach(typeElement
                        -> Optional.of(CodeGenProcessorRegistry.find(element.getQualifiedName().toString()))
                        .ifPresent(genProcessorList ->
                        {

                            for (CodeGenProcessor codeGenProcessor : genProcessorList) {
                                codeGenProcessor.generateClass(typeElement, environment, true);
                            }
                        }));
            });
        } catch (Exception exception) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, "生成异常了");
        }
        return false;
    }

    /**
     * 初始化
     *
     * @param processingEnvironment 加工环境
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        CodeGenProcessorRegistry.initProcessors(processingEnvironment);
        super.init(processingEnvironment);
        ProcessingEnvironmentHolder.setEnvironment(processingEnvironment);
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
        return CodeGenProcessorRegistry.getSupportedAnnotationTypes();
    }

    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }

    public void addProcessor(CodeGenProcessor testProcessor) {
        CodeGenProcessorRegistry.add(testProcessor);
    }
}
