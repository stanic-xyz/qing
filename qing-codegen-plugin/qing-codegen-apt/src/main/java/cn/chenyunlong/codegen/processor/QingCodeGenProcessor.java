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
import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * @author gim
 * @since 2023-10-24
 */
@AutoService(Processor.class)
public class QingCodeGenProcessor extends AbstractProcessor {

    /**
     * 过程
     *
     * @param elements    文档元素列表
     * @param environment 周围环境
     * @return boolean
     */
    @Override
    public boolean process(Set<? extends TypeElement> elements, RoundEnvironment environment) {
        elements.forEach(element ->
        {
            Set<? extends Element> typeElements = environment.getElementsAnnotatedWith(element);
            // 加载需要处理的类的所有注解
            Set<TypeElement> typeElementSet = ElementFilter.typesIn(typeElements);
            Collections.unmodifiableSet(typeElementSet)
                    .forEach(typeElement
                            -> Optional.of(CodeGenProcessorRegistry.find(element.getQualifiedName().toString()))
                            .ifPresent(codeGenProcessor ->
                            {
                                try {
                                    codeGenProcessor.generate(typeElement, environment);
                                } catch (Exception exception) {
                                    String message = "代码生成异常:";
                                    ProcessingEnvironmentHolder.printErrorMessage(message, typeElement, exception);
                                }
                            }));
        });
        return false;
    }

    /**
     * 初始化
     *
     * @param processingEnvironment 加工环境
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        ProcessingEnvironmentHolder.setEnvironment(processingEnvironment);
        CodeGenProcessorRegistry.initProcessors();
    }

    /**
     * 获取支持注释类型
     *
     * @return {@link Set}<{@link String}>
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return CodeGenProcessorRegistry.getSupportedAnnotations();
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
}
