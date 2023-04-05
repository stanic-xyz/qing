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

package cn.chenyunlong.codegen.registry;

import cn.chenyunlong.codegen.context.ProcessingEnvironmentHolder;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import com.google.common.collect.Maps;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * 通过SPI 加载所有的CodeGenProcessor 识别要处理的annotation标记类
 *
 * @author Stan
 * @since 2022/11/28
 */
public final class CodeGenProcessorRegistry {

    private static Map<String, ? extends CodeGenProcessor> processors;


    private CodeGenProcessorRegistry() {
        throw new UnsupportedOperationException();
    }

    /**
     * 注解处理器要处理的注解集合
     *
     * @return {@link Set}<{@link String}>
     */
    public static Set<String> getSupportedAnnotations() {
        return processors.keySet();
    }

    /**
     * 查询通过spi配置进来的代码生成处理器
     *
     * @param annotationClassName 注释类名
     * @return {@link CodeGenProcessor}
     */
    public static CodeGenProcessor find(String annotationClassName) {
        return processors.get(annotationClassName);
    }

    /**
     * 初始化处理器
     * spi 加载所有的processor
     */
    public static void initProcessors() {
        final Map<String, CodeGenProcessor> genProcessorMap = Maps.newLinkedHashMap();
        ServiceLoader<CodeGenProcessor> genProcessors;
        genProcessors = ServiceLoader.load(CodeGenProcessor.class, CodeGenProcessor.class.getClassLoader());
        ProcessingEnvironmentHolder.printMessage("加载apt处理器");
        for (CodeGenProcessor processor : genProcessors) {
            Class<? extends Annotation> annotation = processor.getAnnotation();
            genProcessorMap.put(annotation.getName(), processor);
        }
        // 添加处理器
        CodeGenProcessorRegistry.processors = genProcessorMap;
    }

}
