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

package cn.chenyunlong.codegen.processor.mapper;

import cn.chenyunlong.codegen.annotation.GenMapper;
import cn.chenyunlong.codegen.processor.BaseCodeGenProcessor;
import cn.chenyunlong.codegen.processor.DefaultNameContext;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import cn.chenyunlong.codegen.util.StringUtils;
import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.common.mapper.GenericEnumMapper;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Optional;


/**
 * 生成Mapper
 *
 * @author Stan
 * @date 2022/11/29
 */
@AutoService(value = CodeGenProcessor.class)
public class GenMapperProcessor extends BaseCodeGenProcessor {

    public static final String SUFFIX = "Mapper";

    @Override
    protected void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment, boolean useLombok) {

        DefaultNameContext nameContext = getNameContext(typeElement);

        String className = typeElement.getSimpleName() + SUFFIX;
        String mapperPackageName = nameContext.getMapperPackageName();
        AnnotationSpec mapperAnnotation = AnnotationSpec.builder(Mapper.class)
                .addMember("uses", "$T.class", GenericEnumMapper.class)
                .addMember("uses", "$T.class", DateMapper.class)
                .addMember("unmappedTargetPolicy", "$T.IGNORE", ReportingPolicy.class)
                .build();
        TypeSpec.Builder typeSpecBuilder = TypeSpec.interfaceBuilder(className)
                .addAnnotation(mapperAnnotation)
                .addModifiers(Modifier.PUBLIC);
        FieldSpec instance;
        instance = FieldSpec
                .builder(ClassName.get(mapperPackageName, className), "INSTANCE")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("$T.getMapper($T.class)", Mappers.class, ClassName.get(mapperPackageName, className))
                .build();
        typeSpecBuilder.addField(instance);
        Optional<MethodSpec> dtoToEntityMethod = dtoToEntityMethod(typeElement, nameContext);
        dtoToEntityMethod.ifPresent(typeSpecBuilder::addMethod);
        Optional<MethodSpec> request2UpdaterMethod = request2UpdaterMethod(nameContext);
        request2UpdaterMethod.ifPresent(typeSpecBuilder::addMethod);
        Optional<MethodSpec> request2DtoMethod = request2DtoMethod(nameContext);
        request2DtoMethod.ifPresent(typeSpecBuilder::addMethod);
        Optional<MethodSpec> request2QueryMethod = request2QueryMethod(nameContext);
        request2QueryMethod.ifPresent(typeSpecBuilder::addMethod);
        Optional<MethodSpec> vo2ResponseMethod = vo2ResponseMethod(nameContext);
        vo2ResponseMethod.ifPresent(typeSpecBuilder::addMethod);
        Optional<MethodSpec> vo2CustomResponseMethod = vo2CustomResponseMethod(nameContext);
        vo2CustomResponseMethod.ifPresent(typeSpecBuilder::addMethod);
        GenMapper annotation = typeElement.getAnnotation(GenMapper.class);
        genJavaSourceFile(mapperPackageName, annotation.sourcePath(), typeSpecBuilder, annotation.overrideSource());
    }


    @Override
    public Class<? extends Annotation> getAnnotation() {
        return GenMapper.class;
    }

    @Override
    public String generatePackage(TypeElement typeElement) {
        return typeElement.getAnnotation(GenMapper.class).pkgName();
    }

    private Optional<MethodSpec> dtoToEntityMethod(TypeElement typeElement, DefaultNameContext nameContext) {
        boolean containsNull = StringUtils.containsNull(nameContext.getCreatorPackageName());
        if (!containsNull) {
            return Optional.of(MethodSpec
                    .methodBuilder("dtoToEntity")
                    .returns(ClassName.get(typeElement))
                    .addParameter(
                            ClassName.get(nameContext.getCreatorPackageName(), nameContext.getCreatorClassName()),
                            "dto")
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .build());
        }
        return Optional.empty();
    }

    private Optional<MethodSpec> request2UpdaterMethod(DefaultNameContext nameContext) {
        String updaterPackageName = nameContext.getUpdaterPackageName();
        String updatePackageName = nameContext.getUpdatePackageName();
        boolean containsNull = StringUtils.containsNull(updaterPackageName, updatePackageName);
        if (!containsNull) {
            String updateClassName = nameContext.getUpdateClassName();
            String updaterClassName = nameContext.getUpdaterClassName();
            return Optional.of(MethodSpec
                    .methodBuilder("request2Updater")
                    .returns(ClassName.get(updaterPackageName, updaterClassName))
                    .addParameter(ClassName.get(updatePackageName, updateClassName), "request")
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .build());
        }
        return Optional.empty();
    }

    private Optional<MethodSpec> request2DtoMethod(DefaultNameContext nameContext) {
        String createPackageName = nameContext.getCreatePackageName();
        String creatorPackageName = nameContext.getCreatorPackageName();
        boolean containsNull = StringUtils.containsNull(creatorPackageName, createPackageName);
        if (!containsNull) {
            String creatorClassName = nameContext.getCreatorClassName();
            return Optional.of(MethodSpec
                    .methodBuilder("request2Dto")
                    .returns(ClassName.get(creatorPackageName, creatorClassName))
                    .addParameter(ClassName.get(createPackageName, nameContext.getCreateClassName()), "request")
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .build());
        }
        return Optional.empty();
    }

    private Optional<MethodSpec> request2QueryMethod(DefaultNameContext nameContext) {
        String requestPackageName = nameContext.getQueryRequestPackageName();
        String packageName = nameContext.getQueryPackageName();
        boolean containsNull = StringUtils.containsNull(packageName, requestPackageName);
        if (!containsNull) {
            String queryRequestClassName = nameContext.getQueryRequestClassName();
            return Optional.of(MethodSpec
                    .methodBuilder("request2Query")
                    .returns(ClassName.get(packageName, nameContext.getQueryClassName()))
                    .addParameter(ClassName.get(requestPackageName, queryRequestClassName), "request")
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .build());
        }
        return Optional.empty();
    }

    private Optional<MethodSpec> vo2ResponseMethod(DefaultNameContext nameContext) {
        String responsePackageName = nameContext.getResponsePackageName();
        String voPackageName = nameContext.getVoPackageName();
        boolean containsNull = StringUtils.containsNull(responsePackageName, voPackageName);
        if (!containsNull) {
            String responseClassName = nameContext.getResponseClassName();
            return Optional.of(MethodSpec
                    .methodBuilder("vo2Response")
                    .returns(ClassName.get(responsePackageName, responseClassName))
                    .addParameter(ClassName.get(voPackageName, nameContext.getVoClassName()), "vo")
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .build());
        }
        return Optional.empty();
    }

    private Optional<MethodSpec> vo2CustomResponseMethod(DefaultNameContext nameContext) {
        String responsePackageName = nameContext.getResponsePackageName();
        boolean containsNull = StringUtils.containsNull(responsePackageName, nameContext.getVoPackageName());
        if (!containsNull) {
            String responseClassName = nameContext.getResponseClassName();
            return Optional.of(MethodSpec
                    .methodBuilder("vo2CustomResponse")
                    .returns(ClassName.get(responsePackageName, responseClassName))
                    .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                    .addParameter(ClassName.get(nameContext.getVoPackageName(), nameContext.getVoClassName()), "vo")
                    .addCode(CodeBlock.of("return vo2Response(vo);"))

                    .build());
        }
        return Optional.empty();
    }
}
