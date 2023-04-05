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

package cn.chenyunlong.codegen.processor.api;

import cn.chenyunlong.codegen.processor.BaseCodeGenProcessor;
import cn.chenyunlong.codegen.processor.DefaultNameContext;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import cn.chenyunlong.codegen.util.StringUtils;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import com.google.auto.service.AutoService;
import com.google.common.base.CaseFormat;
import com.squareup.javapoet.*;
import com.squareup.javapoet.TypeSpec.Builder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * 处理Feign接口的代码生成处理器
 *
 * @author Stan
 * @date 2022/11/28
 */
@AutoService(value = CodeGenProcessor.class)
public class GenFeignProcessor extends BaseCodeGenProcessor {

    public static String FEIGN_SUFFIX = "FeignService";

    @Override
    protected void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment, boolean useLombok) {
        DefaultNameContext nameContext = getNameContext(typeElement);
        String classFieldName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL,
                typeElement.getSimpleName().toString());
        GenFeign feign = typeElement.getAnnotation(GenFeign.class);
        Builder builder = TypeSpec.interfaceBuilder(nameContext.getFeignClassName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec
                        .builder(FeignClient.class)
                        .addMember("value", "$S", feign.serverName())
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
        String feignPackageName = nameContext.getFeignPackageName();
        genJavaSourceFile(feignPackageName, typeElement.getAnnotation(GenFeign.class).sourcePath(), builder, true);
    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return GenFeign.class;
    }

    @Override
    public String generatePackage(TypeElement typeElement) {
        return typeElement.getAnnotation(GenFeign.class).pkgName();
    }

    /**
     * 生成create方法
     *
     * @param typeElement 类型元素
     * @param nameContext 名称上下文
     * @return {@link Optional}<{@link MethodSpec}>
     */
    private Optional<MethodSpec> createMethod(TypeElement typeElement, DefaultNameContext nameContext) {
        boolean containsNull = StringUtils.containsNull(nameContext.getCreatePackageName());
        if (!containsNull) {
            return Optional.of(MethodSpec.methodBuilder("create" + typeElement.getSimpleName())
                    .addParameter(
                            ParameterSpec.builder(ClassName.get(nameContext.getCreatePackageName(),
                                    nameContext.getCreateClassName()), "request").addAnnotation(
                                    RequestBody.class).build())
                    .addAnnotation(AnnotationSpec.builder(PostMapping.class).addMember("value", "$S",
                            "create" + typeElement.getSimpleName()).build())
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addJavadoc("createRequest")
                    .returns(ParameterizedTypeName.get(ClassName.get(JsonResult.class), ClassName.get(Long.class))).build());
        }
        return Optional.empty();
    }

    /**
     * 生成公共的<code>update<code/>方法
     *
     * @param typeElement 类型元素
     * @param nameContext 名称上下文
     * @return {@link Optional}<{@link MethodSpec}>
     */
    private Optional<MethodSpec> updateMethod(TypeElement typeElement, DefaultNameContext nameContext) {
        boolean containsNull = StringUtils.containsNull(nameContext.getUpdatePackageName());
        if (!containsNull) {
            return Optional.of(MethodSpec.methodBuilder("update" + typeElement.getSimpleName())
                    .addParameter(ParameterSpec.builder(ClassName.get(nameContext.getUpdatePackageName(),
                            nameContext.getUpdateClassName()), "request").addAnnotation(RequestBody.class).build())
                    .addAnnotation(AnnotationSpec.builder(PostMapping.class).addMember("value", "$S",
                            "update" + typeElement.getSimpleName()).build())
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(ParameterizedTypeName.get(ClassName.get(JsonResult.class), ClassName.get(String.class)))
                    .addJavadoc("update request")
                    .build());
        }
        return Optional.empty();
    }

    /**
     * 检查方法是否有效
     *
     * @param typeElement 类型元素
     * @return {@link Optional}<{@link MethodSpec}>
     */
    private Optional<MethodSpec> validMethod(TypeElement typeElement) {
        return Optional.of(MethodSpec.methodBuilder("valid" + typeElement.getSimpleName())
                .addParameter(ParameterSpec.builder(Long.class, "id").addAnnotation(AnnotationSpec.builder(PathVariable.class).addMember("value", "$S", "id").build()).build())
                .addAnnotation(AnnotationSpec.builder(PostMapping.class).addMember("value", "$S", "valid/{id}").build())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(ParameterizedTypeName.get(ClassName.get(JsonResult.class), ClassName.get(String.class)))
                .addJavadoc("valid")
                .build());
    }

    /**
     * 检查方法是否无效
     *
     * @param typeElement 类型元素
     * @return {@link Optional}<{@link MethodSpec}>
     */
    private Optional<MethodSpec> invalidMethod(TypeElement typeElement) {
        return Optional.of(MethodSpec.methodBuilder("invalid" + typeElement.getSimpleName())
                .addParameter(ParameterSpec.builder(Long.class, "id").addAnnotation(AnnotationSpec.builder(PathVariable.class).addMember("value", "$S", "id").build()).build())
                .addAnnotation(AnnotationSpec.builder(PostMapping.class).addMember("value", "$S", "invalid/{id}").build())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(ParameterizedTypeName.get(ClassName.get(JsonResult.class), ClassName.get(String.class)))
                .addJavadoc("invalid")
                .build());
    }

    /**
     * 按id查找代码生成器
     *
     * @param nameContext 名称上下文
     * @return {@link Optional}<{@link MethodSpec}>
     */
    private Optional<MethodSpec> findById(DefaultNameContext nameContext) {
        boolean containsNull = StringUtils.containsNull(nameContext.getResponsePackageName());
        if (!containsNull) {
            return Optional.of(MethodSpec.methodBuilder("findById")
                    .addParameter(ParameterSpec.builder(Long.class, "id")
                            .addAnnotation(AnnotationSpec.builder(PathVariable.class)
                                    .addMember("value", "$S", "id")
                                    .build())
                            .build())
                    .addAnnotation(AnnotationSpec.builder(GetMapping.class)
                            .addMember("value", "$S", "findById/{id}")
                            .build())
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addJavadoc("findById")
                    .returns(ParameterizedTypeName.get(ClassName.get(JsonResult.class),
                            ClassName.get(nameContext.getResponsePackageName(), nameContext.getResponseClassName())))
                    .build());
        }
        return Optional.empty();
    }

    /**
     * 生成分页查询的方法
     *
     * @param nameContext 代码生成器上下文
     * @return {@link Optional}<{@link MethodSpec}>
     */
    private Optional<MethodSpec> findByPage(DefaultNameContext nameContext) {
        boolean containsNull = StringUtils.containsNull(nameContext.getQueryRequestPackageName(),
                nameContext.getResponsePackageName());
        if (!containsNull) {
            return Optional.of(MethodSpec.methodBuilder("findByPage")
                    .addParameter(ParameterSpec.builder(ParameterizedTypeName.get(ClassName.get(
                            PageRequestWrapper.class), ClassName.get(nameContext.getQueryRequestPackageName(),
                            nameContext.getQueryRequestClassName())), "request").addAnnotation(RequestBody.class).build())
                    .addAnnotation(AnnotationSpec.builder(PostMapping.class).addMember("value", "$S", "findByPage").build())
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .addJavadoc("findByPage request")
                    .returns(ParameterizedTypeName.get(ClassName.get(JsonResult.class),
                            ParameterizedTypeName.get(ClassName.get(
                                    PageResult.class), ClassName.get(nameContext.getResponsePackageName(),
                                    nameContext.getResponseClassName()))))
                    .build());
        }
        return Optional.empty();
    }
}
