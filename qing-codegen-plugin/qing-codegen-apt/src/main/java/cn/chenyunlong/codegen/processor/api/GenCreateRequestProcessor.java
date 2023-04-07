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

import cn.chenyunlong.codegen.annotation.GenCreateRequest;
import cn.chenyunlong.codegen.annotation.IgnoreCreator;
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
 * 支持创建器的生成器
 *
 * @author chenyunlong
 * @date 2019/11/28 19:33
 */

@SupportedGenTypes(types = IgnoreCreator.class)
public class GenCreateRequestProcessor extends BaseCodeGenProcessor {

    public static final String CREATE_REQUEST_SUFFIX = "CreateRequest";

    @Override
    public void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment, boolean useLombok) {
        DefaultNameContext nameContext = getNameContext(typeElement);

        String queryRequestPackageName = nameContext.getQueryRequestPackageName();

        Set<VariableElement> fields = findFields(typeElement,
                element -> Objects.isNull(element.getAnnotation(IgnoreCreator.class)));
        TypeSpec.Builder typeSpecBuilder = TypeSpec
                .classBuilder(nameContext.getCreateClassName())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Request.class)
                .addAnnotation(Schema.class);
        if (useLombok) {
            typeSpecBuilder.addAnnotation(Data.class);
        }
        addSetterAndGetterMethodWithConverter(typeSpecBuilder, fields, useLombok);

        genJavaSourceFile(queryRequestPackageName,
                typeElement.getAnnotation(GenCreateRequest.class).sourcePath(), typeSpecBuilder, true);
    }


    @Override
    public String generatePackage(TypeElement typeElement) {
        return typeElement.getAnnotation(GenCreateRequest.class).pkgName();
    }
}
