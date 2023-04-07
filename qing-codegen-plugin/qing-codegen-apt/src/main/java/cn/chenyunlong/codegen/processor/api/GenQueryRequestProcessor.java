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

package cn.chenyunlong.codegen.processor.api;

import cn.chenyunlong.codegen.annotation.GenQueryRequest;
import cn.chenyunlong.codegen.annotation.QueryItem;
import cn.chenyunlong.codegen.annotation.SupportedGenTypes;
import cn.chenyunlong.codegen.processor.BaseCodeGenProcessor;
import cn.chenyunlong.codegen.processor.DefaultNameContext;
import cn.chenyunlong.common.model.Request;
import com.squareup.javapoet.TypeSpec;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.Objects;
import java.util.Set;

/**
 * 处理QueryRequest的代码生成
 *
 * @author Stan
 * @date 2022/11/28
 */
@SupportedGenTypes(types = GenQueryRequest.class)
public class GenQueryRequestProcessor extends BaseCodeGenProcessor {

    public static String QUERY_REQUEST_SUFFIX = "QueryRequest";

    @Override
    public void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment, boolean useLombok) {
        DefaultNameContext nameContext = getNameContext(typeElement);

        String queryRequestPackageName = nameContext.getQueryRequestPackageName();

        Set<VariableElement> fields = findFields(typeElement,
                element -> Objects.nonNull(element.getAnnotation(QueryItem.class)));
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(nameContext.getQueryRequestClassName())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Request.class)
                .addAnnotation(Schema.class);
        if (useLombok) {
            typeSpecBuilder.addAnnotation(Data.class);
        }
        addSetterAndGetterMethodWithConverter(typeSpecBuilder, fields, useLombok);
        genJavaSourceFile(queryRequestPackageName,
                typeElement.getAnnotation(GenQueryRequest.class).sourcePath(), typeSpecBuilder, true);
    }

    @Override
    public String generatePackage(TypeElement typeElement) {
        return typeElement.getAnnotation(GenQueryRequest.class).pkgName();
    }
}
