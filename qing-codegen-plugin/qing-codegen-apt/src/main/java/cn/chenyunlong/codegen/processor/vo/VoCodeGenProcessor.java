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

package cn.chenyunlong.codegen.processor.vo;

import cn.chenyunlong.codegen.processor.BaseCodeGenProcessor;
import com.google.auto.service.AutoService;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Set;

/**
 * @author gim vo 代码生成器
 */
@AutoService(value = CodeGenProcessor.class)
public class VoCodeGenProcessor extends BaseCodeGenProcessor {

    public static final String SUFFIX = "VO";

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return GenVo.class;
    }

    @Override
    public String generatePackage(TypeElement typeElement) {
        return typeElement.getAnnotation(GenVo.class).pkgName();
    }

    @Override
    protected void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment) {
        Set<VariableElement> fields = findFields(typeElement,
                ve -> Objects.isNull(ve.getAnnotation(IgnoreVo.class)));
        String className = PREFIX + typeElement.getSimpleName() + SUFFIX;
        String sourceClassName = typeElement.getSimpleName() + SUFFIX;
        Builder builder = TypeSpec.classBuilder(className)
                .superclass(AbstractBaseJpaVO.class)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Schema.class)
                .addAnnotation(Data.class);
        addSetterAndGetterMethod(builder, fields);
        MethodSpec.Builder constructorSpecBuilder = MethodSpec.constructorBuilder()
                .addParameter(TypeName.get(typeElement.asType()), "source")
                .addModifiers(Modifier.PUBLIC);
        constructorSpecBuilder.addStatement("super(source)");
        fields.forEach(variableElement -> constructorSpecBuilder.addStatement("this.set$L(source.get$L())", getFieldDefaultName(variableElement),
                getFieldDefaultName(variableElement)));
        builder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED)
                .build());
        builder.addMethod(constructorSpecBuilder.build());
        String packageName = generatePackage(typeElement);
        genJavaFile(packageName, builder);
        genJavaFile(packageName, getSourceTypeWithConstruct(typeElement, sourceClassName, packageName, className));
    }
}