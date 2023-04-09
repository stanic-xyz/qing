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
import cn.chenyunlong.codegen.processor.api.*;
import cn.chenyunlong.codegen.processor.controller.GenControllerProcessor;
import cn.chenyunlong.codegen.processor.creator.GenCreatorProcessor;
import cn.chenyunlong.codegen.processor.mapper.GenMapperProcessor;
import cn.chenyunlong.codegen.processor.query.GenQueryProcessor;
import cn.chenyunlong.codegen.processor.repository.GenRepositoryProcessor;
import cn.chenyunlong.codegen.processor.service.GenServiceImplProcessor;
import cn.chenyunlong.codegen.processor.service.GenServiceProcessor;
import cn.chenyunlong.codegen.processor.updater.GenUpdaterProcessor;
import cn.chenyunlong.codegen.processor.vo.GenVoCodeProcessor;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 通过SPI 加载所有的CodeGenProcessor 识别要处理的annotation标记类
 *
 * @author Stan
 * @since 2022/11/28
 */
public final class CodeGenProcessorRegistry {


    private static final List<CodeGenProcessor> PROCESSOR_LIST = new LinkedList<>();

    static {
        PROCESSOR_LIST.add(new GenUpdateRequestProcessor());
        PROCESSOR_LIST.add(new GenCreateRequestProcessor());
        PROCESSOR_LIST.add(new GenQueryRequestProcessor());
        PROCESSOR_LIST.add(new GenResponseProcessor());
        PROCESSOR_LIST.add(new GenControllerProcessor());
        PROCESSOR_LIST.add(new GenServiceProcessor());
        PROCESSOR_LIST.add(new GenServiceImplProcessor());
        PROCESSOR_LIST.add(new GenRepositoryProcessor());
        PROCESSOR_LIST.add(new GenFeignProcessor());
        PROCESSOR_LIST.add(new GenMapperProcessor());
        PROCESSOR_LIST.add(new GenUpdaterProcessor());
        PROCESSOR_LIST.add(new GenVoCodeProcessor());
        PROCESSOR_LIST.add(new GenQueryProcessor());
        PROCESSOR_LIST.add(new GenCreatorProcessor());
    }


    private CodeGenProcessorRegistry() {
        throw new UnsupportedOperationException();
    }


    /**
     * 查询通过spi配置进来的代码生成处理器
     *
     * @param annotationClassName 注释类名
     * @return {@link CodeGenProcessor}
     */
    public static List<CodeGenProcessor> find(String annotationClassName) {
        return PROCESSOR_LIST.stream().filter(codeGenProcessor -> codeGenProcessor.support(annotationClassName)).collect(Collectors.toList());
    }

    /**
     * 初始化处理器
     * spi 加载所有的processor
     */
    public static void initProcessors(ProcessingEnvironment processingEnvironment) {
        ProcessingEnvironmentHolder.setEnvironment(processingEnvironment);
        ProcessingEnvironmentHolder.printMessage("加载apt处理器");
        for (CodeGenProcessor codeGenProcessor : PROCESSOR_LIST) {
            codeGenProcessor.init(processingEnvironment);
        }
    }

    public static Set<String> getSupportedAnnotationTypes() {
        return PROCESSOR_LIST.stream()
                .map(CodeGenProcessor::getSupportedAnnotation)
                .filter(Objects::nonNull).map(Class::getCanonicalName)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * 添加处理器
     *
     * @param codeGenProcessor 处理器
     */
    public static void add(CodeGenProcessor codeGenProcessor) {
        PROCESSOR_LIST.add(codeGenProcessor);
    }
}
