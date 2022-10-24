/*
 * Copyright (c) 2019-2022 YunLong Chen
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

import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import com.google.common.collect.Maps;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * @author gim 通过SPI 加载所有的CodeGenProcessor 识别要处理的annotation标记类
 */
public final class CodeGenProcessorRegistry {

    private static Map<String, ? extends CodeGenProcessor> PROCESSORS;


    private CodeGenProcessorRegistry() {
        throw new UnsupportedOperationException();
    }

    /**
     * 注解处理器要处理的注解集合
     *
     * @return {@link Set}<{@link String}>
     */
    public static Set<String> getSupportedAnnotations() {
        return PROCESSORS.keySet();
    }

    public static CodeGenProcessor find(String annotationClassName) {
        return PROCESSORS.get(annotationClassName);
    }

    /**
     * 初始化处理器
     * spi 加载所有的processor
     */
    public static void initProcessors() {
        final Map<String, CodeGenProcessor> map = Maps.newLinkedHashMap();
        ServiceLoader<CodeGenProcessor> processors = ServiceLoader.load(CodeGenProcessor.class, CodeGenProcessor.class.getClassLoader());
        for (CodeGenProcessor next : processors) {
            Class<? extends Annotation> annotation = next.getAnnotation();
            map.put(annotation.getName(), next);
        }
        PROCESSORS = map;
    }

}
