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

package cn.chenyunlong.codegen.processor.controller;


import cn.chenyunlong.codegen.annotation.GenController;
import cn.chenyunlong.codegen.annotation.SupportedGenTypes;
import cn.chenyunlong.codegen.context.NameContext;
import cn.chenyunlong.codegen.processor.AbstractCodeGenProcessor;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import cn.chenyunlong.codegen.util.StringUtils;
import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.model.JsonResult;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.common.model.PageResult;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @author gim
 * 获取名称时可以先获取上下文再取，不用一个个的取，这样更方便
 */
@AutoService(CodeGenProcessor.class)
@SupportedGenTypes(types = GenController.class)
public class GenControllerProcessor extends AbstractCodeGenProcessor {

    public static final String CONTROLLER_SUFFIX = "Controller";

    @Override
    public void generateClass(TypeElement typeElement, RoundEnvironment roundEnv,
                              boolean useLombok) {
        NameContext nameContext = getNameContext(typeElement);

        String serviceFieldName =
            StringUtils.lowerCamel(typeElement.getSimpleName().toString()) + "Service";
        String serviceClassName = nameContext.getServiceClassName();
        String servicePackageName = nameContext.getServicePackageName();
        String controllerClassName = nameContext.getControllerClassName();
        String controllerPackageName = nameContext.getControllerPackageName();

        if (StringUtils.containsNull(servicePackageName)) {
            return;
        }

        TypeSpec.Builder builder = TypeSpec
            .classBuilder(controllerClassName)
            .addAnnotation(RestController.class)
            .addAnnotation(Slf4j.class)
            .addAnnotation(AnnotationSpec
                .builder(RequestMapping.class)
                .addMember("value", "$S", "api/v1/" + StringUtils.lowerUnderscore(typeElement
                    .getSimpleName()
                    .toString()))
                .build())
            .addAnnotation(RequiredArgsConstructor.class)
            .addModifiers(Modifier.PUBLIC)
            .addField(FieldSpec
                .builder(ClassName.get(servicePackageName, serviceClassName), serviceFieldName)
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .build());

        createMethod(serviceFieldName, typeElement, nameContext).ifPresent(builder::addMethod);
        updateMethod(serviceFieldName, typeElement, nameContext).ifPresent(builder::addMethod);
        validMethod(serviceFieldName, typeElement).ifPresent(builder::addMethod);
        inValidMethod(serviceFieldName, typeElement).ifPresent(builder::addMethod);
        findById(serviceFieldName, nameContext).ifPresent(builder::addMethod);
        page(serviceFieldName, nameContext).ifPresent(builder::addMethod);
        genJavaSourceFile(typeElement, builder);
    }

    /**
     * 创建方法
     *
     * @param serviceFieldName 服务字段名
     * @param typeElement      类型元素
     * @param nameContext      命名上下文
     * @return {@link Optional}<{@link MethodSpec}>
     */
    private Optional<MethodSpec> createMethod(String serviceFieldName, TypeElement typeElement,
                                              NameContext nameContext) {
        String creatorPackageName = nameContext.getCreatorPackageName();
        String queryRequestPackageName = nameContext.getQueryRequestPackageName();
        String mapperPackageName = nameContext.getMapperPackageName();

        boolean containsNull = StringUtils.containsNull(queryRequestPackageName, creatorPackageName,
            mapperPackageName);
        if (!containsNull) {

            String creatorClassName = nameContext.getCreatorClassName();
            String mapperClassName = nameContext.getMapperClassName();
            String createClassName = nameContext.getCreateClassName();

            MethodSpec.Builder createMethodBuilder =
                MethodSpec.methodBuilder("create" + typeElement.getSimpleName());
            createMethodBuilder
                .addParameter(ParameterSpec
                    .builder(ClassName.get(queryRequestPackageName, createClassName), "request")
                    .addAnnotation(RequestBody.class)
                    .build())
                .addAnnotation(AnnotationSpec.builder(PostMapping.class).build())
                .addModifiers(Modifier.PUBLIC)
                .addCode(CodeBlock.of("$T creator = $T.INSTANCE.request2Dto(request);",
                    ClassName.get(creatorPackageName, creatorClassName),
                    ClassName.get(mapperPackageName, mapperClassName)))
                .addCode(CodeBlock.of("return $T.success($L.create$L(creator));", JsonResult.class,
                    serviceFieldName, typeElement
                        .getSimpleName()
                        .toString()))
                .addJavadoc("createRequest")
                .returns(ParameterizedTypeName.get(ClassName.get(JsonResult.class),
                    ClassName.get(Long.class)));
            return Optional.of(createMethodBuilder.build());
        }
        return Optional.empty();
    }

    /**
     * 更新方法
     *
     * @param serviceFieldName 服务字段名
     * @param typeElement      类型元素
     * @param nameContext      命名上下文
     * @return {@link Optional}<{@link MethodSpec}>
     */
    private Optional<MethodSpec> updateMethod(String serviceFieldName, TypeElement typeElement,
                                              NameContext nameContext) {
        String updatePackageName = nameContext.getUpdatePackageName();
        String updaterPackageName = nameContext.getUpdaterPackageName();
        String mapperPackageName = nameContext.getMapperPackageName();
        boolean containsNull =
            StringUtils.containsNull(updatePackageName, updaterPackageName, mapperPackageName);
        if (!containsNull) {
            String updateClassName = nameContext.getUpdateClassName();
            String updaterClassName = nameContext.getUpdaterClassName();
            String mapperClassName = nameContext.getMapperClassName();
            Name className = typeElement.getSimpleName();
            return Optional.of(MethodSpec
                .methodBuilder("update" + className)
                .addParameter(ParameterSpec
                    .builder(ClassName.get(updatePackageName, updateClassName), "request")
                    .addAnnotation(RequestBody.class)
                    .build())
                .addAnnotation(AnnotationSpec
                    .builder(PostMapping.class)
                    .addMember("value", "$S", "update" + className)
                    .build())
                .addModifiers(Modifier.PUBLIC)
                .addCode(CodeBlock.of("$T updater = $T.INSTANCE.request2Updater(request);",
                    ClassName.get(updaterPackageName, updaterClassName),
                    ClassName.get(mapperPackageName, mapperClassName)))
                .addCode(
                    CodeBlock.of("$L.update$L(updater);\n", serviceFieldName, className.toString()))
                .addCode(CodeBlock.of("return $T.success($T.Success.getName());", JsonResult.class,
                    CodeEnum.class))
                .returns(ParameterizedTypeName.get(ClassName.get(JsonResult.class),
                    ClassName.get(String.class)))
                .addJavadoc("update request")
                .build());
        }
        return Optional.empty();
    }

    /**
     * 启用
     *
     * @param serviceFieldName 服务字段名
     * @param typeElement      类型元素
     * @return {@link Optional}<{@link MethodSpec}>
     */
    private Optional<MethodSpec> validMethod(String serviceFieldName, TypeElement typeElement) {
        return Optional.of(MethodSpec
            .methodBuilder("valid" + typeElement.getSimpleName())
            .addParameter(
                ParameterSpec.builder(Long.class, "id").addAnnotation(PathVariable.class).build())
            .addAnnotation(
                AnnotationSpec.builder(PostMapping.class).addMember("value", "$S", "valid/{id}")
                    .build())
            .addModifiers(Modifier.PUBLIC)
            .addCode(CodeBlock.of("$L.valid$L(id);", serviceFieldName,
                typeElement.getSimpleName().toString()))
            .addCode(CodeBlock.of("return $T.success($T.Success.getName());", JsonResult.class,
                CodeEnum.class))
            .returns(ParameterizedTypeName.get(ClassName.get(JsonResult.class),
                ClassName.get(String.class)))
            .addJavadoc("valid")
            .build());
    }

    /**
     * 修复不返回方法的问题
     *
     * @param serviceFieldName 服务字段名
     * @param typeElement      类型元素
     * @return {@link Optional}<{@link MethodSpec}>
     */
    private Optional<MethodSpec> inValidMethod(String serviceFieldName, TypeElement typeElement) {
        return Optional.of(MethodSpec
            .methodBuilder("invalid" + typeElement.getSimpleName())
            .addParameter(
                ParameterSpec.builder(Long.class, "id").addAnnotation(PathVariable.class).build())
            .addAnnotation(AnnotationSpec
                .builder(PostMapping.class)
                .addMember("value", "$S", "invalid/{id}")
                .build())
            .addModifiers(Modifier.PUBLIC)
            .addCode(CodeBlock.of("$L.invalid$L(id);", serviceFieldName,
                typeElement.getSimpleName().toString()))
            .addCode(CodeBlock.of("return $T.success($T.Success.getName());", JsonResult.class,
                CodeEnum.class))
            .returns(ParameterizedTypeName.get(ClassName.get(JsonResult.class),
                ClassName.get(String.class)))
            .addJavadoc("invalid")
            .build());
    }

    /**
     * 通过id查询
     *
     * @param serviceFieldName 服务字段名
     * @param nameContext      命名上下文
     * @return {@link Optional}<{@link MethodSpec}>
     */
    private Optional<MethodSpec> findById(String serviceFieldName, NameContext nameContext) {
        String voPackageName = nameContext.getVoPackageName();
        String responsePackageName = nameContext.getResponsePackageName();
        String mapperPackageName = nameContext.getMapperPackageName();
        boolean containsNull =
            StringUtils.containsNull(voPackageName, responsePackageName, mapperPackageName);
        if (!containsNull) {
            String voClassName = nameContext.getVoClassName();
            String responseClassName = nameContext.getResponseClassName();
            return Optional.of(MethodSpec
                .methodBuilder("findById")
                .addParameter(
                    ParameterSpec.builder(Long.class, "id").addAnnotation(PathVariable.class)
                        .build())
                .addAnnotation(AnnotationSpec
                    .builder(GetMapping.class)
                    .addMember("value", "$S", "findById/{id}")
                    .build())
                .addModifiers(Modifier.PUBLIC)
                .addCode(CodeBlock.of("$T vo = $L.findById(id);",
                    ClassName.get(voPackageName, voClassName), serviceFieldName))
                .addCode(CodeBlock.of("$T response = $T.INSTANCE.vo2CustomResponse(vo);",
                    ClassName.get(responsePackageName, responseClassName),
                    ClassName.get(mapperPackageName, nameContext.getMapperClassName())))
                .addCode(CodeBlock.of("return $T.success(response);", JsonResult.class))
                .addJavadoc("findById")
                .returns(ParameterizedTypeName.get(ClassName.get(JsonResult.class),
                    ClassName.get(responsePackageName, responseClassName)))
                .build());
        }
        return Optional.empty();
    }

    /**
     * 分页查询
     *
     * @param serviceFieldName 服务字段名
     * @param nameContext      命名上下文
     * @return {@link Optional}<{@link MethodSpec}>
     */
    private Optional<MethodSpec> page(String serviceFieldName, NameContext nameContext) {
        boolean containsNull =
            StringUtils.containsNull(nameContext.getQueryRequestPackageName(),
                nameContext.getQueryPackageName(), nameContext.getMapperPackageName(),
                nameContext.getVoPackageName(), nameContext.getResponsePackageName());
        if (!containsNull) {
            return Optional.of(MethodSpec
                .methodBuilder("page")
                .addParameter(ParameterSpec
                    .builder(ParameterizedTypeName.get(ClassName.get(PageRequestWrapper.class),
                        ClassName.get(nameContext.getQueryRequestPackageName(),
                            nameContext.getQueryRequestClassName())), "request")
                    .addAnnotation(RequestBody.class)
                    .build())
                .addAnnotation(AnnotationSpec
                    .builder(PostMapping.class)
                    .addMember("value", "$S", "findByPage")
                    .build())
                .addModifiers(Modifier.PUBLIC)
                .addCode(CodeBlock.of("$T<$T> wrapper = new $T<>();\n", PageRequestWrapper.class,
                    ClassName.get(nameContext.getQueryPackageName(),
                        nameContext.getQueryClassName()), PageRequestWrapper.class))
                .addCode(
                    CodeBlock.of("wrapper.setBean($T.INSTANCE.request2Query(request.getBean()));\n",
                        ClassName.get(nameContext.getMapperPackageName(),
                            nameContext.getMapperClassName())))
                .addCode(CodeBlock.of("""
                    wrapper.setSorts(request.getSorts());
                        wrapper.setPageSize(request.getPageSize());
                        wrapper.setPage(request.getPage());
                    """))
                .addCode(CodeBlock.of("$T<$T> page = $L.findByPage(wrapper);\n", Page.class,
                    ClassName.get(nameContext.getVoPackageName(), nameContext.getVoClassName()),
                    serviceFieldName))
                .addCode(CodeBlock.of("""
                        return $T.success(
                                $T.of(
                                    page.getContent().stream()
                                        .map($T.INSTANCE::vo2CustomResponse)
                                        .collect($T.toList()),
                                    page.getTotalElements(),
                                    page.getSize(),
                                    page.getNumber())
                            );
                        """, JsonResult.class, PageResult.class,
                    ClassName.get(nameContext.getMapperPackageName(),
                        nameContext.getMapperClassName()), Collectors.class))
                .addJavadoc("findByPage request")
                .returns(ParameterizedTypeName.get(ClassName.get(JsonResult.class),
                    ParameterizedTypeName.get(ClassName.get(PageResult.class),
                        ClassName.get(nameContext.getResponsePackageName(),
                            nameContext.getResponseClassName()))))
                .build());
        }
        return Optional.empty();
    }

    /**
     * 获取子包名称
     *
     * @param typeElement 类型元素
     * @return 生成的文件package
     */
    @Override
    public String getSubPackageName(TypeElement typeElement) {
        return "controller";
    }
}
