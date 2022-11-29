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

package cn.chenyunlong.codegen.processor.mapper;

import cn.chenyunlong.codegen.processor.BaseCodeGenProcessor;
import cn.chenyunlong.codegen.processor.DefaultNameContext;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import cn.chenyunlong.codegen.util.StringUtils;
import cn.chenyunlong.common.mapper.DateMapper;
import cn.chenyunlong.common.mapper.GenericEnumMapper;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import org.mapstruct.Mapper;
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
    protected void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment) {

        DefaultNameContext nameContext = getNameContext(typeElement);

        String className = typeElement.getSimpleName() + SUFFIX;
        String mapperPackageName = nameContext.getMapperPackageName();
        AnnotationSpec mapperAnnotation = AnnotationSpec.builder(Mapper.class)
                .addMember("uses", "$T.class", GenericEnumMapper.class)
                .addMember("uses", "$T.class", DateMapper.class)
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
        genJavaSourceFile(mapperPackageName, typeElement.getAnnotation(GenMapper.class).sourcePath(), typeSpecBuilder);
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
        boolean containsNull = StringUtils.containsNull(nameContext.getUpdaterPackageName(), nameContext.getUpdatePackageName());
        if (!containsNull) {
            return Optional.of(MethodSpec
                    .methodBuilder("request2Updater")
                    .returns(
                            ClassName.get(nameContext.getUpdaterPackageName(), nameContext.getUpdaterClassName()))
                    .addParameter(
                            ClassName.get(nameContext.getUpdatePackageName(), nameContext.getUpdateClassName()),
                            "request")
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .build());
        }
        return Optional.empty();
    }

    private Optional<MethodSpec> request2DtoMethod(DefaultNameContext nameContext) {
        boolean containsNull = StringUtils.containsNull(nameContext.getCreatorPackageName(), nameContext.getCreatePackageName());
        if (!containsNull) {
            return Optional.of(MethodSpec
                    .methodBuilder("request2Dto")
                    .returns(
                            ClassName.get(nameContext.getCreatorPackageName(), nameContext.getCreatorClassName()))
                    .addParameter(
                            ClassName.get(nameContext.getCreatePackageName(), nameContext.getCreateClassName()),
                            "request")
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .build());
        }
        return Optional.empty();
    }

    private Optional<MethodSpec> request2QueryMethod(DefaultNameContext nameContext) {
        boolean containsNull = StringUtils.containsNull(nameContext.getQueryPackageName(), nameContext.getQueryRequestPackageName());
        if (!containsNull) {
            return Optional.of(MethodSpec
                    .methodBuilder("request2Query")
                    .returns(
                            ClassName.get(nameContext.getQueryPackageName(), nameContext.getQueryClassName()))
                    .addParameter(ClassName.get(nameContext.getQueryRequestPackageName(),
                            nameContext.getQueryRequestClassName()), "request")
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .build());
        }
        return Optional.empty();
    }

    private Optional<MethodSpec> vo2ResponseMethod(DefaultNameContext nameContext) {
        boolean containsNull = StringUtils.containsNull(nameContext.getResponsePackageName(), nameContext.getVoPackageName());
        if (!containsNull) {
            return Optional.of(MethodSpec
                    .methodBuilder("vo2Response")
                    .returns(ClassName.get(nameContext.getResponsePackageName(),
                            nameContext.getResponseClassName()))
                    .addParameter(ClassName.get(nameContext.getVoPackageName(), nameContext.getVoClassName()),
                            "vo")
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .build());
        }
        return Optional.empty();
    }

    private Optional<MethodSpec> vo2CustomResponseMethod(DefaultNameContext nameContext) {
        boolean containsNull = StringUtils.containsNull(nameContext.getResponsePackageName(), nameContext.getVoPackageName());
        if (!containsNull) {
            return Optional.of(MethodSpec
                    .methodBuilder("vo2CustomResponse")
                    .returns(ClassName.get(nameContext.getResponsePackageName(),
                            nameContext.getResponseClassName()))
                    .addParameter(ClassName.get(nameContext.getVoPackageName(), nameContext.getVoClassName()),
                            "vo")
                    .addCode(
                            CodeBlock.of("$T response = vo2Response(vo);\n",
                                    ClassName.get(nameContext.getResponsePackageName(),
                                            nameContext.getResponseClassName()))
                    )
                    .addCode(
                            CodeBlock.of("return response;")
                    )
                    .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
                    .build());
        }
        return Optional.empty();
    }
}
