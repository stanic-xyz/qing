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

package cn.chenyunlong.codegen.handller.api;

import cn.chenyunlong.codegen.annotation.GenFeign;
import cn.chenyunlong.codegen.annotation.SupportedGenTypes;
import cn.chenyunlong.codegen.context.NameContext;
import cn.chenyunlong.codegen.handller.AbstractCodeGenProcessor;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import cn.chenyunlong.codegen.util.StringUtils;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import cn.chenyunlong.qing.domain.common.AggregateId;
import com.google.auto.service.AutoService;
import com.google.common.base.CaseFormat;
import org.springframework.javapoet.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Optional;

/**
 * 处理Feign接口的代码生成处理器。
 *
 * @author 陈云龙
 * @since 2022/11/28
 */
@AutoService(CodeGenProcessor.class)
@SupportedGenTypes(types = GenFeign.class)
public class GenFeignProcessor extends AbstractCodeGenProcessor {

    public static String FEIGN_SUFFIX = "FeignService";

    @Override
    public void generateClass(TypeElement typeElement, RoundEnvironment roundEnv,
                              boolean useLombok) {
        NameContext nameContext = getNameContext(typeElement);
        String classFieldName =
            CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL,
                typeElement.getSimpleName().toString());
        GenFeign annotation = typeElement.getAnnotation(GenFeign.class);
        TypeSpec.Builder builder = TypeSpec
            .interfaceBuilder(nameContext.getFeignClassName())
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(AnnotationSpec
                .builder(ClassName.get("org.springframework.cloud.openfeign", "FeignClient"))
                .addMember("value", "$S", annotation.serverName())
                .addMember("contextId", "$S", classFieldName + "Client")
                .addMember("path", "$S", classFieldName + "/v1")
                .build());
        Optional<MethodSpec> createMethod = createMethod(typeElement, nameContext);
        createMethod.ifPresent(builder::addMethod);
        Optional<MethodSpec> updateMethod = updateMethod(typeElement, nameContext);
        updateMethod.ifPresent(builder::addMethod);
        Optional<MethodSpec> validMethod = validMethod(typeElement);
        validMethod.ifPresent(builder::addMethod);
        Optional<MethodSpec> invalidMethod = invalidMethod(typeElement);
        invalidMethod.ifPresent(builder::addMethod);
        Optional<MethodSpec> findById = findById(nameContext);
        findById.ifPresent(builder::addMethod);
        Optional<MethodSpec> findByPage = findByPage(nameContext);
        findByPage.ifPresent(builder::addMethod);
        genJavaSourceFile(typeElement, builder);
    }

    /**
     * 生成创建方法。
     *
     * @param typeElement 类型元素
     * @param nameContext 名称上下文
     * @return {@link Optional}<{@link MethodSpec}>
     */
    private Optional<MethodSpec> createMethod(TypeElement typeElement, NameContext nameContext) {
        boolean containsNull = StringUtils.containsNull(nameContext.getCreatePackageName());
        if (!containsNull) {
            ClassName requestBody =
                ClassName.get("org.springframework.web.bind.annotation", "RequestBody");
            return Optional.of(MethodSpec
                .methodBuilder("create" + typeElement.getSimpleName())
                .addParameter(ParameterSpec
                    .builder(ClassName.get(nameContext.getCreatePackageName(),
                        nameContext.getCreateClassName()), "request")
                    .addAnnotation(requestBody)
                    .build())
                .addAnnotation(AnnotationSpec
                    .builder(PostMapping.class)
                    .addMember("value", "$S", "create" + typeElement.getSimpleName())
                    .build())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addJavadoc("创建")
                .returns(ParameterizedTypeName.get(ClassName.get(JsonResult.class),
                    ClassName.get(Long.class)))
                .build());
        }
        return Optional.empty();
    }

    /**
     * 生成公共的<code>update</code>方法。
     *
     * @param typeElement 类型元素
     * @param nameContext 名称上下文
     * @return {@link Optional}<{@link MethodSpec}>
     */
    private Optional<MethodSpec> updateMethod(TypeElement typeElement, NameContext nameContext) {
        boolean containsNull = StringUtils.containsNull(nameContext.getUpdatePackageName());
        if (!containsNull) {
            ClassName requestBody =
                ClassName.get("org.springframework.web.bind.annotation", "RequestBody");
            return Optional.of(MethodSpec
                .methodBuilder("update" + typeElement.getSimpleName())
                .addParameter(ParameterSpec
                    .builder(ClassName.get(nameContext.getUpdatePackageName(),
                        nameContext.getUpdateClassName()), "request")
                    .addAnnotation(requestBody)
                    .build())
                .addAnnotation(AnnotationSpec
                    .builder(PostMapping.class)
                    .addMember("value", "$S", "update" + typeElement.getSimpleName())
                    .build())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(ParameterizedTypeName.get(ClassName.get(JsonResult.class),
                    ClassName.get(String.class)))
                .addJavadoc("更新请求")
                .build());
        }
        return Optional.empty();
    }

    /**
     * 检查方法是否有效。
     *
     * @param typeElement 类型元素
     * @return {@link Optional}<{@link MethodSpec}>
     */
    private Optional<MethodSpec> validMethod(TypeElement typeElement) {
        return Optional.of(MethodSpec
            .methodBuilder("valid" + typeElement.getSimpleName())
            .addParameter(ParameterSpec
                .builder(Long.class, "id")
                .addAnnotation(AnnotationSpec
                    .builder(PathVariable.class)
                    .addMember("value", "$S", "id")
                    .build())
                .build())
            .addAnnotation(
                AnnotationSpec.builder(PostMapping.class).addMember("value", "$S", "valid/{id}")
                    .build())
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(ParameterizedTypeName.get(ClassName.get(JsonResult.class),
                ClassName.get(String.class)))
            .addJavadoc("有效")
            .build());
    }

    /**
     * 检查方法是否无效。
     *
     * @param typeElement 类型元素
     * @return {@link Optional}<{@link MethodSpec}>
     */
    private Optional<MethodSpec> invalidMethod(TypeElement typeElement) {
        return Optional.of(MethodSpec
            .methodBuilder("invalid" + typeElement.getSimpleName())
            .addParameter(ParameterSpec
                .builder(Long.class, "id")
                .addAnnotation(AnnotationSpec
                    .builder(PathVariable.class)
                    .addMember("value", "$S", "id")
                    .build())
                .build())
            .addAnnotation(AnnotationSpec
                .builder(PostMapping.class)
                .addMember("value", "$S", "invalid/{id}")
                .build())
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(ParameterizedTypeName.get(ClassName.get(JsonResult.class),
                ClassName.get(String.class)))
            .addJavadoc("无效")
            .build());
    }

    /**
     * 按id查找代码生成器。
     *
     * @param nameContext 名称上下文
     * @return {@link Optional}<{@link MethodSpec}>
     */
    private Optional<MethodSpec> findById(NameContext nameContext) {
        boolean containsNull = StringUtils.containsNull(nameContext.getResponsePackageName());
        if (!containsNull) {
            return Optional.of(MethodSpec
                .methodBuilder("findById")
                .addParameter(ParameterSpec
                    .builder(Long.class, "id")
                    .addAnnotation(AnnotationSpec
                        .builder(PathVariable.class)
                        .addMember("value", "$S", "id")
                        .build())
                    .build())
                .addAnnotation(AnnotationSpec
                    .builder(GetMapping.class)
                    .addMember("value", "$S", "findById/{id}")
                    .build())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addJavadoc("根据ID查询")
                .returns(ParameterizedTypeName.get(ClassName.get(JsonResult.class),
                    ClassName.get(nameContext.getResponsePackageName(),
                        nameContext.getResponseClassName())))
                .build());
        }
        return Optional.empty();
    }

    /**
     * 生成分页查询的方法。
     *
     * @param nameContext 代码生成器上下文
     * @return {@link Optional}<{@link MethodSpec}>
     */
    private Optional<MethodSpec> findByPage(NameContext nameContext) {
        boolean containsNull =
            StringUtils.containsNull(nameContext.getQueryRequestPackageName(),
                nameContext.getResponsePackageName());
        if (!containsNull) {
            ClassName requestBody =
                ClassName.get("org.springframework.web.bind.annotation", "RequestBody");
            return Optional.of(MethodSpec
                .methodBuilder("page")
                .addParameter(ParameterSpec
                    .builder(ParameterizedTypeName.get(ClassName.get(PageRequestWrapper.class),
                        ClassName.get(nameContext.getQueryRequestPackageName(),
                            nameContext.getQueryRequestClassName())), "request")
                    .addAnnotation(requestBody)
                    .build())
                .addAnnotation(AnnotationSpec
                    .builder(PostMapping.class)
                    .addMember("value", "$S", "findByPage")
                    .build())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addJavadoc("分页查询")
                .returns(ParameterizedTypeName.get(ClassName.get(JsonResult.class),
                    ParameterizedTypeName.get(ClassName.get(PageResult.class),
                        ClassName.get(nameContext.getResponsePackageName(),
                            nameContext.getResponseClassName()))))
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
        return typeElement.getAnnotation(GenFeign.class).pkgName();
    }

    @Override
    public String getSourcePath(TypeElement typeElement) {
        return typeElement.getAnnotation(GenFeign.class).sourcePath();
    }
}
