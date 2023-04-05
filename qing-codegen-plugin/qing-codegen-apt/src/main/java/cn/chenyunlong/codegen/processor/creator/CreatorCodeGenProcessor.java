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

package cn.chenyunlong.codegen.processor.creator;

import cn.chenyunlong.codegen.processor.BaseCodeGenProcessor;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author gim Creator 代码生成器
 */
@AutoService(value = CodeGenProcessor.class)
public class CreatorCodeGenProcessor extends BaseCodeGenProcessor {

    public static final String SUFFIX = "Creator";
    // 这里为什么要忽略这些类型呢
    static final List<TypeName> dtoIgnoreFieldTypes = new ArrayList<>();

    static {
        dtoIgnoreFieldTypes.add(TypeName.get(Date.class));
        dtoIgnoreFieldTypes.add(TypeName.get(LocalDateTime.class));
    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return GenCreator.class;
    }

    @Override
    public String generatePackage(TypeElement typeElement) {
        return typeElement.getAnnotation(GenCreator.class).pkgName();
    }

    /**
     * 生成类
     *
     * @param typeElement      类型元素
     * @param roundEnvironment 周围环境
     * @param useLombok        使用lombok
     */
    @Override
    public void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment, boolean useLombok) {
        // lombok - mapstruct 集成
        String sourceClassName = typeElement.getSimpleName() + SUFFIX;
        Builder classBuilder = TypeSpec.classBuilder(sourceClassName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Schema.class);
        if (useLombok) {
            classBuilder.addAnnotation(Data.class);
        }
        addSetterAndGetterMethod(classBuilder,
                findFields(typeElement, variableElement
                        -> Objects.isNull(variableElement.getAnnotation(IgnoreCreator.class)) && !dtoIgnore(variableElement)), useLombok);
        String packageName = getNameContext(typeElement).getCreatorPackageName();
        genJavaSourceFile(packageName, typeElement.getAnnotation(GenCreator.class).sourcePath(), classBuilder, true);
    }

    /**
     * 忽略dto生成
     *
     * @param element 元素
     * @return boolean
     */
    private boolean dtoIgnore(Element element) {
        return dtoIgnoreFieldTypes.contains(TypeName.get(element.asType())) || element.getModifiers()
                .contains(Modifier.STATIC);
    }


}
