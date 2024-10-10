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

package cn.chenyunlong.codegen.handller.dto;

import cn.chenyunlong.codegen.annotation.GenVo;
import cn.chenyunlong.codegen.annotation.IgnoreVo;
import cn.chenyunlong.codegen.annotation.SupportedGenTypes;
import cn.chenyunlong.codegen.handller.AbstractCodeGenProcessor;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.Objects;
import java.util.Set;

/**
 * vo 代码生成器。
 *
 * @author gim
 */
@AutoService(CodeGenProcessor.class)
@SupportedGenTypes(types = GenVo.class)
public class GenVoCodeProcessor extends AbstractCodeGenProcessor {

    public static final String SUFFIX = "VO";


    @Override
    public void generateClass(TypeElement typeElement, RoundEnvironment roundEnv,
                              boolean useLombok) {
        Set<VariableElement> fields =
            findFields(typeElement,
                variableElement -> Objects.isNull(variableElement.getAnnotation(IgnoreVo.class)));
        String sourceClassName = typeElement.getSimpleName() + SUFFIX;
        Builder builder = TypeSpec
            .classBuilder(sourceClassName)
            .superclass(AbstractBaseJpaVo.class)
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Schema.class);
        if (useLombok) {
            builder.addAnnotation(Data.class);
            builder.addAnnotation(///////////
                AnnotationSpec.builder(EqualsAndHashCode.class).addMember("callSuper", "$L", true)
                    .build());
            builder.addAnnotation(AnnotationSpec
                .builder(NoArgsConstructor.class)
                .addMember("access", "$T.PROTECTED", AccessLevel.class)
                .build());
        }
        addSetterAndGetterMethod(builder, fields);
        MethodSpec.Builder constructorSpecBuilder = MethodSpec
            .constructorBuilder()
            .addParameter(TypeName.get(typeElement.asType()), "source")
            .addModifiers(Modifier.PUBLIC);
        constructorSpecBuilder.addStatement("super()")
            .addStatement("this.setId(source.getId());")
            .addStatement("this.setCreatedAt(source.getCreatedAt());")
            .addStatement("this.setUpdatedAt(source.getCreatedAt());")
            .addStatement("this.setVersion(source.getVersion());");

        fields.forEach(
            variableElement -> constructorSpecBuilder.addStatement("this.set$L(source.get$L())",
                getFieldDefaultName(variableElement), getFieldDefaultName(variableElement)));
        builder.addMethod(constructorSpecBuilder.build());
        genJavaSourceFile(typeElement, builder);
    }

    /**
     * 获取子包名称。
     *
     * @param typeElement 类型元素
     * @return 生成的文件package
     */
    @Override
    public String getSubPackageName(TypeElement typeElement) {
        return typeElement.getAnnotation(GenVo.class).pkgName();
    }

    @Override
    public String getSourcePath(TypeElement typeElement) {
        return typeElement.getAnnotation(GenVo.class).sourcePath();
    }
}
