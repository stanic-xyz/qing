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

package cn.chenyunlong.codegen.handller.mapper;

import cn.chenyunlong.codegen.annotation.GenMapper;
import cn.chenyunlong.codegen.annotation.SupportedGenTypes;
import cn.chenyunlong.codegen.context.NameContext;
import cn.chenyunlong.codegen.handller.AbstractCodeGenProcessor;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import cn.chenyunlong.codegen.util.StringUtils;
import cn.hutool.core.bean.BeanUtil;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import java.util.Optional;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;


/**
 * 生成Mapper。
 *
 * @author 陈云龙
 * @since 2022/11/29
 */
@AutoService(CodeGenProcessor.class)
@SupportedGenTypes(types = GenMapper.class)
public class GenMapperProcessor extends AbstractCodeGenProcessor {

    public static final String SUFFIX = "Mapper";

    @Override
    public void generateClass(TypeElement typeElement, RoundEnvironment roundEnv,
                              boolean useLombok) {

        NameContext nameContext = getNameContext(typeElement);

        String className = typeElement.getSimpleName() + SUFFIX;
        String mapperPackageName = nameContext.getMapperPackageName();
        TypeSpec.Builder builder =
            TypeSpec.interfaceBuilder(className).addModifiers(Modifier.PUBLIC);
        FieldSpec instance;
        ClassName type = ClassName.get(mapperPackageName, className);
        instance = FieldSpec
            .builder(type, "INSTANCE")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .initializer("new $T() {}", type)
            .build();
        builder.addField(instance);
        Optional<MethodSpec> dtoToEntityMethod = dtoToEntityMethod(typeElement, nameContext);
        dtoToEntityMethod.ifPresent(builder::addMethod);
        Optional<MethodSpec> request2UpdaterMethod = request2UpdaterMethod(nameContext);
        request2UpdaterMethod.ifPresent(builder::addMethod);
        Optional<MethodSpec> request2DtoMethod = request2DtoMethod(nameContext);
        request2DtoMethod.ifPresent(builder::addMethod);
        Optional<MethodSpec> request2QueryMethod = request2QueryMethod(nameContext);
        request2QueryMethod.ifPresent(builder::addMethod);
        Optional<MethodSpec> vo2ResponseMethod = vo2ResponseMethod(nameContext);
        vo2ResponseMethod.ifPresent(builder::addMethod);
        Optional<MethodSpec> vo2CustomResponseMethod = vo2CustomResponseMethod(nameContext);
        vo2CustomResponseMethod.ifPresent(builder::addMethod);
        genJavaSourceFile(typeElement, builder);
    }

    private Optional<MethodSpec> dtoToEntityMethod(TypeElement typeElement,
                                                   NameContext nameContext) {
        String packageName = nameContext.getCreatorPackageName();
        boolean containsNull = StringUtils.containsNull(packageName);
        if (!containsNull) {
            ClassName returnType = ClassName.get(typeElement);
            return Optional.of(MethodSpec
                .methodBuilder("dtoToEntity")
                .returns(returnType)
                .addParameter(ClassName.get(packageName, nameContext.getCreatorClassName()), "dto")
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                .addCode(CodeBlock.of("return $T.copyProperties(dto, $T.class);", BeanUtil.class,
                    returnType))
                .build());
        }
        return Optional.empty();
    }

    private Optional<MethodSpec> request2UpdaterMethod(NameContext nameContext) {
        String updaterPackageName = nameContext.getUpdaterPackageName();
        String updatePackageName = nameContext.getUpdatePackageName();
        boolean containsNull = StringUtils.containsNull(updaterPackageName, updatePackageName);
        if (!containsNull) {
            String updateClassName = nameContext.getUpdateClassName();
            String updaterClassName = nameContext.getUpdaterClassName();
            ClassName returnType = ClassName.get(updaterPackageName, updaterClassName);
            return Optional.of(MethodSpec
                .methodBuilder("request2Updater")
                .returns(returnType)
                .addParameter(ClassName.get(updatePackageName, updateClassName), "request")
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                .addCode(
                    CodeBlock.of("return $T.copyProperties(request, $T.class);", BeanUtil.class,
                        returnType))
                .build());
        }
        return Optional.empty();
    }

    private Optional<MethodSpec> request2DtoMethod(NameContext nameContext) {
        String createPackageName = nameContext.getCreatePackageName();
        String creatorPackageName = nameContext.getCreatorPackageName();
        boolean containsNull = StringUtils.containsNull(creatorPackageName, createPackageName);
        if (!containsNull) {
            String creatorClassName = nameContext.getCreatorClassName();
            ClassName returnType = ClassName.get(creatorPackageName, creatorClassName);
            return Optional.of(MethodSpec
                .methodBuilder("request2Dto")
                .returns(returnType)
                .addParameter(ClassName.get(createPackageName, nameContext.getCreateClassName()),
                    "request")
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                .addCode(
                    CodeBlock.of("return $T.copyProperties(request, $T.class);", BeanUtil.class,
                        returnType))
                .build());
        }
        return Optional.empty();
    }

    private Optional<MethodSpec> request2QueryMethod(NameContext nameContext) {
        String requestPackageName = nameContext.getQueryRequestPackageName();
        String packageName = nameContext.getQueryPackageName();
        boolean containsNull = StringUtils.containsNull(packageName, requestPackageName);
        if (!containsNull) {
            String queryRequestClassName = nameContext.getQueryRequestClassName();
            ClassName returnType = ClassName.get(packageName, nameContext.getQueryClassName());
            return Optional.of(MethodSpec
                .methodBuilder("request2Query")
                .returns(returnType)
                .addParameter(ClassName.get(requestPackageName, queryRequestClassName), "request")
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                .addCode(
                    CodeBlock.of("return $T.copyProperties(request, $T.class);", BeanUtil.class,
                        returnType))
                .build());
        }
        return Optional.empty();
    }

    private Optional<MethodSpec> vo2ResponseMethod(NameContext nameContext) {
        String responsePackageName = nameContext.getResponsePackageName();
        String voPackageName = nameContext.getVoPackageName();
        boolean containsNull = StringUtils.containsNull(responsePackageName, voPackageName);
        if (!containsNull) {
            String responseClassName = nameContext.getResponseClassName();
            ClassName returnType = ClassName.get(responsePackageName, responseClassName);
            return Optional.of(MethodSpec
                .methodBuilder("vo2Response")
                .returns(returnType)
                .addParameter(ClassName.get(voPackageName, nameContext.getVoClassName()), "vo")
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                .addCode(CodeBlock.of("return $T.copyProperties(vo, $T.class);", BeanUtil.class,
                    returnType))
                .build());
        }
        return Optional.empty();
    }

    private Optional<MethodSpec> vo2CustomResponseMethod(NameContext nameContext) {
        String responsePackageName = nameContext.getResponsePackageName();
        boolean containsNull =
            StringUtils.containsNull(responsePackageName, nameContext.getVoPackageName());
        if (!containsNull) {
            String responseClassName = nameContext.getResponseClassName();
            return Optional.of(MethodSpec
                .methodBuilder("vo2CustomResponse")
                .returns(ClassName.get(responsePackageName, responseClassName))
                .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                .addParameter(
                    ClassName.get(nameContext.getVoPackageName(), nameContext.getVoClassName()),
                    "vo")
                .addCode(CodeBlock.of("return vo2Response(vo);"))
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
        return typeElement.getAnnotation(GenMapper.class).pkgName();
    }

    @Override
    public String getSourcePath(TypeElement typeElement) {
        return typeElement.getAnnotation(GenMapper.class).sourcePath();
    }
}
