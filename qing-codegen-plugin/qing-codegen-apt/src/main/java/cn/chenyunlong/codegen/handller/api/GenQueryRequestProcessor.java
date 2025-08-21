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

package cn.chenyunlong.codegen.handller.api;

import cn.chenyunlong.codegen.annotation.GenQueryRequest;
import cn.chenyunlong.codegen.annotation.QueryItem;
import cn.chenyunlong.codegen.annotation.SupportedGenTypes;
import cn.chenyunlong.codegen.cache.CacheStrategy;
import cn.chenyunlong.codegen.context.NameContext;
import cn.chenyunlong.codegen.handller.AbstractCodeGenProcessor;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import cn.chenyunlong.common.model.Request;
import com.google.auto.service.AutoService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.javapoet.TypeSpec;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.Objects;
import java.util.Set;

/**
 * 处理QueryRequest的代码生成
 *
 * @author 陈云龙
 * @since 2022/11/28
 */
@AutoService(CodeGenProcessor.class)
@SupportedGenTypes(types = GenQueryRequest.class, cacheStrategy = CacheStrategy.SMART)
public class GenQueryRequestProcessor extends AbstractCodeGenProcessor {

    public static String QUERY_REQUEST_SUFFIX = "QueryRequest";

    @Override
    public void generateClass(TypeElement typeElement, RoundEnvironment roundEnv,
                              boolean useLombok) {
        NameContext nameContext = getNameContext(typeElement);

        Set<VariableElement> fields = findFields(typeElement,
            element -> Objects.nonNull(element.getAnnotation(QueryItem.class)));
        TypeSpec.Builder builder = TypeSpec
            .classBuilder(nameContext.getQueryRequestClassName())
            .addModifiers(Modifier.PUBLIC)
            .addSuperinterface(Request.class)
            .addAnnotation(Schema.class);
        if (useLombok) {
            builder.addAnnotation(Data.class);
        }
        addSetterAndGetterMethodWithConverter(builder, fields);
        genJavaSourceFile(typeElement, builder);
    }

    /**
     * 获取子包名称
     * /**
     * 获取子包名称。
     *
     * @param typeElement 类型元素
     * @return 生成的文件package
     */
    @Override
    public String getSubPackageName(TypeElement typeElement) {
        return typeElement.getAnnotation(GenQueryRequest.class).pkgName();
    }

    @Override
    public String getSourcePath(TypeElement typeElement) {
        return typeElement.getAnnotation(GenQueryRequest.class).sourcePath();
    }
}
