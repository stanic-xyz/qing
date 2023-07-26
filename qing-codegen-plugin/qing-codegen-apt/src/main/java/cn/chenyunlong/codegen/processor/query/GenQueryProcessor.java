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

package cn.chenyunlong.codegen.processor.query;

import cn.chenyunlong.codegen.annotation.GenQuery;
import cn.chenyunlong.codegen.annotation.QueryItem;
import cn.chenyunlong.codegen.annotation.SupportedGenTypes;
import cn.chenyunlong.codegen.processor.AbstractCodeGenProcessor;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.TypeSpec;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Objects;

/**
 * 生成Query方法
 *
 * @author cyl
 * @date 2019-10-08 17:14
 */
@AutoService(CodeGenProcessor.class)
@SupportedGenTypes(types = GenQuery.class)
public class GenQueryProcessor extends AbstractCodeGenProcessor {

    public static String QUERY_SUFFIX = "Query";

    /**
     * 生成类
     * 生成Class
     *
     * @param typeElement      顶层元素
     * @param roundEnvironment 周围环境
     * @param useLombok        是否使用lombok
     */
    @Override
    public void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment, boolean useLombok) {

        String sourceClassName = typeElement.getSimpleName() + QUERY_SUFFIX;

        TypeSpec.Builder builder =
                TypeSpec.classBuilder(sourceClassName).addModifiers(Modifier.PUBLIC).addAnnotation(Schema.class);

        if (useLombok) {
            builder.addAnnotation(Data.class);
        }
        addSetterAndGetterMethod(builder, findFields(typeElement, variableElement -> Objects.nonNull(variableElement.getAnnotation(QueryItem.class))), useLombok);
        genJavaSourceFile(typeElement, builder, true);
    }

    /**
     * 获取子包名称
     *
     * @param typeElement 类型元素
     * @return 生成的文件package
     */
    @Override
    public String getSubPackageName(TypeElement typeElement) {
        return "query";
    }

}
