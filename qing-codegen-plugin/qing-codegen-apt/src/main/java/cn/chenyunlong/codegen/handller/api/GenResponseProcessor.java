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

import cn.chenyunlong.codegen.annotation.GenResponse;
import cn.chenyunlong.codegen.annotation.IgnoreVo;
import cn.chenyunlong.codegen.annotation.SupportedGenTypes;
import cn.chenyunlong.codegen.context.NameContext;
import cn.chenyunlong.codegen.handller.AbstractCodeGenProcessor;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import cn.chenyunlong.common.model.AbstractJpaResponse;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeSpec;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * 处理Response的代码生成
 *
 * @author Stan
 * @since 2022/11/28
 */
@AutoService(CodeGenProcessor.class)
@SupportedGenTypes(types = GenResponse.class)
public class GenResponseProcessor extends AbstractCodeGenProcessor {

    public static String RESPONSE_SUFFIX = "Response";

    @Override
    public void generateClass(TypeElement typeElement, RoundEnvironment roundEnv,
                              boolean useLombok) {
        NameContext nameContext = getNameContext(typeElement);
        Set<VariableElement> fields =
            findFields(typeElement,
                variableElement -> Objects.isNull(variableElement.getAnnotation(IgnoreVo.class)));
        TypeSpec.Builder builder = TypeSpec
            .classBuilder(nameContext.getResponseClassName())
            .addModifiers(Modifier.PUBLIC)
            .superclass(AbstractJpaResponse.class)
            .addAnnotation(Schema.class)
            .addStaticBlock(CodeBlock.builder().build());
        addSetterAndGetterMethodWithConverter(builder, fields);
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
        return typeElement.getAnnotation(GenResponse.class).pkgName();
    }

    @Override
    public String getSourcePath(TypeElement typeElement) {
        return typeElement.getAnnotation(GenResponse.class).sourcePath();
    }

}
