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

package cn.chenyunlong.codegen.processor.updater;

import cn.chenyunlong.codegen.annotation.GenUpdater;
import cn.chenyunlong.codegen.annotation.IgnoreUpdater;
import cn.chenyunlong.codegen.processor.BaseCodeGenProcessor;
import cn.chenyunlong.codegen.processor.DefaultNameContext;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import com.google.auto.service.AutoService;
import com.google.common.base.CaseFormat;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * 更新一代处理器
 *
 * @author Gim
 * @date 2019/11/28 19:33
 */

@AutoService(value = CodeGenProcessor.class)
public class GenUpdaterProcessor extends BaseCodeGenProcessor {

    public static final String SUFFIX = "Updater";

    /**
     * 生成类
     *
     * @param typeElement      类型元素
     * @param roundEnvironment 周围环境
     * @param useLombok        使用lombok
     */
    @Override
    protected void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment, boolean useLombok) {

        Set<VariableElement> variableElements;
        variableElements = findFields(typeElement,
                element -> Objects.isNull(element.getAnnotation(IgnoreUpdater.class)));
        String className = PREFIX + typeElement.getSimpleName() + SUFFIX;
        String sourceClassName = typeElement.getSimpleName() + SUFFIX;
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(sourceClassName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Schema.class);

        if (useLombok) {
            classBuilder.addAnnotation(Data.class);
        }

        addSetterAndGetterMethod(classBuilder, variableElements, useLombok);
        CodeBlock.Builder builder = CodeBlock.builder();
        for (VariableElement variableElement : variableElements) {
            builder.addStatement("$T.ofNullable($L()).ifPresent(v -> param.$L(v))", Optional.class,
                    "get" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL,
                            variableElement.getSimpleName().toString()),
                    "set" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL,
                            variableElement.getSimpleName().toString()));
        }
        MethodSpec.Builder methodBuilder;
        methodBuilder = MethodSpec.methodBuilder(
                        "update" + typeElement.getSimpleName())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(typeElement.asType()), "param")
                .addCode(builder.build())
                .returns(void.class);
        classBuilder.addMethod(methodBuilder.build());

        addIdField(classBuilder, useLombok);

        DefaultNameContext nameContext = getNameContext(typeElement);
        String packageName = nameContext.getUpdaterPackageName();
        genJavaSourceFile(packageName, typeElement.getAnnotation(GenUpdater.class).sourcePath(), classBuilder, true);
    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return GenUpdater.class;
    }

    @Override
    public String generatePackage(TypeElement typeElement) {
        return typeElement.getAnnotation(GenUpdater.class).pkgName();
    }
}
