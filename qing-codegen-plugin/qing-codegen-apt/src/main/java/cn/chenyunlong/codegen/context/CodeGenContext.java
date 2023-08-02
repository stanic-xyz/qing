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

import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.processing.ProcessingEnvironment;

/**
 * 通过SPI 加载所有的CodeGenProcessor 识别要处理的annotation标记类。
 *
 * @author Stan
 * @since 2022/11/28
 */
public final class CodeGenContext {

    private static final Map<String, CodeGenProcessor> PROCESSOR_MAP = new LinkedHashMap<>();
    private static NameContext nameContext;

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
        return PROCESSOR_MAP
            .entrySet()
            .stream()
            .filter(stringCodeGenProcessorEntry -> annotationClassName.contains(
                stringCodeGenProcessorEntry.getKey()))
            .map(Map.Entry::getValue)
            .collect(Collectors.toSet());
    }

    /**
     * 初始化处理器。
     * spi 加载所有的processor
     */
    public static void init(ProcessingEnvironment processingEnvironment) {
        nameContext = new NameContext();
        nameContext.init();
        ServiceLoader<CodeGenProcessor> codeGenProcessors =
            ServiceLoader.load(CodeGenProcessor.class, CodeGenProcessor.class.getClassLoader());
        for (CodeGenProcessor codeGenProcessor : codeGenProcessors) {
            ProcessingEnvironmentHolder.printMessage(
                "[codegen-plugin]：加载处理器：%s".formatted(codeGenProcessor
                    .getClass()
                    .getName()));
            codeGenProcessor.init(processingEnvironment);
            PROCESSOR_MAP.put(codeGenProcessor.getSupportedAnnotation().getName(),
                codeGenProcessor);
        }
    }

    public static Set<String> getSupportedAnnotationTypes() {
        return PROCESSOR_MAP.keySet();
    }
}
