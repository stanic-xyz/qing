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

import cn.chenyunlong.codegen.annotation.GenUpdateRequest;
import cn.chenyunlong.codegen.annotation.IgnoreUpdater;
import cn.chenyunlong.codegen.annotation.SupportedGenTypes;
import cn.chenyunlong.codegen.context.NameContext;
import cn.chenyunlong.codegen.processor.AbstractCodeGenProcessor;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import cn.chenyunlong.common.model.Request;
import com.google.auto.service.AutoService;
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
 * 更新接口参数代码生成器
 *
 * @author Stan
 * @date 2022/11/29
 */
@AutoService(value = CodeGenProcessor.class)
@SupportedGenTypes(types = GenUpdateRequest.class)
public class GenUpdateRequestProcessor extends AbstractCodeGenProcessor {

    public static String UPDATE_REQUEST_SUFFIX = "UpdateRequest";

    @Override
    public void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment, boolean useLombok) {
        NameContext nameContext = getNameContext(typeElement);
        Set<VariableElement> fields =
                findFields(typeElement, element -> Objects.isNull(element.getAnnotation(IgnoreUpdater.class)));
        TypeSpec.Builder builder = TypeSpec
                .classBuilder(nameContext.getUpdateClassName())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Request.class)
                .addAnnotation(Schema.class);
        if (useLombok) {
            builder.addAnnotation(Data.class);
        }
        addSetterAndGetterMethodWithConverter(builder, fields, useLombok);
        addIdField(builder, useLombok);
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
        return "request";
    }

}
