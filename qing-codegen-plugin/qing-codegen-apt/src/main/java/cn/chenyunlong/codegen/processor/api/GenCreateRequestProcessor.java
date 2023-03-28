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

import cn.chenyunlong.codegen.processor.BaseCodeGenProcessor;
import cn.chenyunlong.codegen.processor.DefaultNameContext;
import cn.chenyunlong.codegen.processor.creator.IgnoreCreator;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import cn.chenyunlong.common.model.Request;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.TypeSpec;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Set;

/**
 * @Author: Gim
 * @Date: 2019/11/28 19:33
 * @Description:
 */

@AutoService(value = CodeGenProcessor.class)
public class GenCreateRequestProcessor extends BaseCodeGenProcessor {

    public static final String CREATE_REQUEST_SUFFIX = "CreateRequest";

    @Override
    protected void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment) {
        DefaultNameContext nameContext = getNameContext(typeElement);

        String queryRequestPackageName = nameContext.getQueryRequestPackageName();

        Set<VariableElement> fields = findFields(typeElement,
                element -> Objects.isNull(element.getAnnotation(IgnoreCreator.class)));
        TypeSpec.Builder typeSpecBuilder;
        typeSpecBuilder = TypeSpec
                .classBuilder(nameContext.getCreateClassName())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Request.class)
                .addAnnotation(Schema.class);
        addSetterAndGetterMethodWithConverter(typeSpecBuilder, fields);

        genJavaSourceFile(queryRequestPackageName,
                typeElement.getAnnotation(GenCreateRequest.class).sourcePath(), typeSpecBuilder);
    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return GenCreateRequest.class;
    }

    @Override
    public String generatePackage(TypeElement typeElement) {
        return typeElement.getAnnotation(GenCreateRequest.class).pkgName();
    }
}
