/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.codegen.handller.service;


import cn.chenyunlong.codegen.annotation.GenService;
import cn.chenyunlong.codegen.annotation.SupportedGenTypes;
import cn.chenyunlong.codegen.cache.CacheStrategy;
import cn.chenyunlong.codegen.context.NameContext;
import cn.chenyunlong.codegen.handller.AbstractCodeGenProcessor;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import cn.chenyunlong.codegen.util.StringUtils;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.common.AggregateId;
import com.google.auto.service.AutoService;
import org.springframework.data.domain.Page;
import org.springframework.javapoet.ClassName;
import org.springframework.javapoet.MethodSpec;
import org.springframework.javapoet.ParameterizedTypeName;
import org.springframework.javapoet.TypeSpec;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Optional;

/**
 * Service 处理器。
 *
 * @author gim
 */
@AutoService(CodeGenProcessor.class)
@SupportedGenTypes(types = GenService.class, cacheStrategy = CacheStrategy.SKIP_IF_EXISTS)
public class GenServiceProcessor extends AbstractCodeGenProcessor {

    public static final String SERVICE_SUFFIX = "Service";

    public static final String SERVICE_PREFIX = "I";

    @Override
    public void generateClass(TypeElement typeElement, RoundEnvironment roundEnv,
                              boolean useLombok) {

        NameContext nameContext = getNameContext(typeElement);

        String className = SERVICE_PREFIX + typeElement.getSimpleName() + SERVICE_SUFFIX;
        TypeSpec.Builder builder;
        builder = TypeSpec.interfaceBuilder(className).addModifiers(Modifier.PUBLIC);
        createMethod(typeElement, nameContext).ifPresent(builder::addMethod);
        updateMethod(typeElement, nameContext).ifPresent(builder::addMethod);
        validMethod(typeElement).ifPresent(builder::addMethod);
        invalidMethod(typeElement).ifPresent(builder::addMethod);
        findByIdMethod(nameContext).ifPresent(builder::addMethod);
        findByPageMethod(nameContext).ifPresent(builder::addMethod);
        genJavaSourceFile(typeElement, builder);
    }

    private Optional<MethodSpec> createMethod(TypeElement typeElement, NameContext nameContext) {
        boolean containsNull = StringUtils.containsNull(nameContext.getCreatorPackageName());
        if (!containsNull) {
            return Optional.of(MethodSpec
                .methodBuilder("create" + typeElement.getSimpleName())
                .addParameter(ClassName.get(nameContext.getCreatorPackageName(),
                    nameContext.getCreatorClassName()), "creator")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addJavadoc("create")
                .returns(Long.class)
                .build());
        }
        return Optional.empty();
    }

    private Optional<MethodSpec> updateMethod(TypeElement typeElement, NameContext nameContext) {
        boolean containsNull = StringUtils.containsNull(nameContext.getUpdaterPackageName());
        if (!containsNull) {
            return Optional.of(MethodSpec
                .methodBuilder("update" + typeElement.getSimpleName())
                .addParameter(ClassName.get(nameContext.getUpdaterPackageName(),
                    nameContext.getUpdaterClassName()), "updater")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addJavadoc("update")
                .build());
        }
        return Optional.empty();
    }

    private Optional<MethodSpec> validMethod(TypeElement typeElement) {
        return Optional.of(MethodSpec
            .methodBuilder("valid" + typeElement.getSimpleName())
            .addParameter(Long.class, "id")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .addJavadoc("valid")
            .build());
    }

    private Optional<MethodSpec> invalidMethod(TypeElement typeElement) {
        return Optional.of(MethodSpec
            .methodBuilder("invalid" + typeElement.getSimpleName())
            .addParameter(Long.class, "id")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .addJavadoc("invalid")
            .build());
    }

    private Optional<MethodSpec> findByIdMethod(NameContext nameContext) {
        boolean containsNull = StringUtils.containsNull(nameContext.getVoPackageName());
        if (!containsNull) {
            return Optional.of(MethodSpec
                .methodBuilder("findById")
                .addParameter(Long.class, "id")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addJavadoc("findById")
                .returns(
                    ClassName.get(nameContext.getVoPackageName(), nameContext.getVoClassName()))
                .build());
        }
        return Optional.empty();
    }

    private Optional<MethodSpec> findByPageMethod(NameContext nameContext) {
        boolean containsNull =
            StringUtils.containsNull(nameContext.getQueryPackageName(),
                nameContext.getVoPackageName());
        if (!containsNull) {
            return Optional.of(MethodSpec
                .methodBuilder("findByPage")
                .addParameter(ParameterizedTypeName.get(ClassName.get(PageRequestWrapper.class),
                    ClassName.get(nameContext.getQueryPackageName(),
                        nameContext.getQueryClassName())), "query")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addJavadoc("findByPage")
                .returns(ParameterizedTypeName.get(ClassName.get(Page.class),
                    ClassName.get(nameContext.getVoPackageName(), nameContext.getVoClassName())))
                .build());
        }
        return Optional.empty();
    }

    /**
     * 获取子包名称。
     *
     * @param typeElement 类型元素
     * @return 生成的文件package
     */
    @Override
    public String getSubPackageName(TypeElement typeElement) {
        return typeElement.getAnnotation(GenService.class).pkgName();
    }

    @Override
    public String getSourcePath(TypeElement typeElement) {
        return typeElement.getAnnotation(GenService.class).sourcePath();
    }

}
