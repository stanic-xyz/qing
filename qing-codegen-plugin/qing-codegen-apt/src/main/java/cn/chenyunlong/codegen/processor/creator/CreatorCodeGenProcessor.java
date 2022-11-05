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

package cn.chenyunlong.codegen.processor.creator;

import cn.chenyunlong.codegen.processor.BaseCodeGenProcessor;
import com.google.auto.service.AutoService;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
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
    static final List<TypeName> dtoIgnoreFieldTypes;

    static {
        dtoIgnoreFieldTypes = new ArrayList<>();
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
     */
    @Override
    public void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment) {
        /*
          为啥加@Data 还要生成get set 方法？
          lombok - mapstruct 集成
         */
        String className = PREFIX + typeElement.getSimpleName() + SUFFIX;
        String sourceClassName = typeElement.getSimpleName() + SUFFIX;
        Builder classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Schema.class)
                .addAnnotation(Data.class);
        addSetterAndGetterMethod(classBuilder, findFields(typeElement, element -> Objects.isNull(element.getAnnotation(
                IgnoreCreator.class)) && !dtoIgnore(element)));
        String packageName = generatePackage(typeElement);
        genJavaFile(packageName, classBuilder);
        genJavaFile(packageName, getSourceType(sourceClassName, packageName, className));
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