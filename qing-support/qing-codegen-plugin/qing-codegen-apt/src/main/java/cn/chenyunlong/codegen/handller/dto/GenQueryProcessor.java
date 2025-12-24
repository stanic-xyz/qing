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

import cn.chenyunlong.codegen.annotation.GenQuery;
import cn.chenyunlong.codegen.annotation.QueryItem;
import cn.chenyunlong.codegen.annotation.SupportedGenTypes;
import cn.chenyunlong.codegen.cache.CacheStrategy;
import cn.chenyunlong.codegen.handller.AbstractCodeGenProcessor;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import com.google.auto.service.AutoService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.javapoet.AnnotationSpec;
import org.springframework.javapoet.TypeSpec;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Objects;

/**
 * 生成Query方法
 *
 * @author cyl
 * @since 2019-10-08 17:14
 */
@AutoService(CodeGenProcessor.class)
@SupportedGenTypes(types = GenQuery.class, cacheStrategy = CacheStrategy.SMART)
public class GenQueryProcessor extends AbstractCodeGenProcessor {

    public static String QUERY_SUFFIX = "Query";

    /**
     * 生成类
     * 生成Class
     *
     * @param typeElement 顶层元素
     * @param roundEnv    周围环境
     * @param useLombok   是否使用lombok
     */
    @Override
    public void generateClass(TypeElement typeElement, RoundEnvironment roundEnv,
                              boolean useLombok) {

        String sourceClassName = typeElement.getSimpleName() + QUERY_SUFFIX;

        // 构建Schema注解
        AnnotationSpec schemaAnnotation = AnnotationSpec.builder(Schema.class)
            .addMember("name", "$S", sourceClassName)
            .addMember("description", "$S", typeElement.getSimpleName() + "查询条件对象")
            .build();

        TypeSpec.Builder builder =
            TypeSpec.classBuilder(sourceClassName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(schemaAnnotation)
                .addJavadoc("$L查询条件对象\n\n@author 代码生成器\n@since $L\n", 
                    typeElement.getSimpleName(), 
                    java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        if (useLombok) {
            builder.addAnnotation(Data.class);
        }
        addSetterAndGetterMethod(builder, findFields(typeElement,
            variableElement -> Objects.nonNull(variableElement.getAnnotation(QueryItem.class)))
        );
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
        return typeElement.getAnnotation(GenQuery.class).pkgName();
    }

    @Override
    public String getSourcePath(TypeElement typeElement) {
        return typeElement.getAnnotation(GenQuery.class).sourcePath();
    }

}
