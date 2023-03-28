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

package cn.chenyunlong.codegen.processor.repository;


import cn.chenyunlong.codegen.processor.BaseCodeGenProcessor;
import cn.chenyunlong.codegen.processor.DefaultNameContext;
import cn.chenyunlong.codegen.spi.CodeGenProcessor;
import cn.chenyunlong.jpa.support.BaseRepository;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;

/**
 * 生成Repository层的代码
 *
 * @author gim
 * @date 2022/12/22
 */
@AutoService(value = CodeGenProcessor.class)
public class GenRepositoryProcessor extends BaseCodeGenProcessor {

    public static final String REPOSITORY_SUFFIX = "Repository";

    @Override
    protected void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment) {
        String className = typeElement.getSimpleName() + REPOSITORY_SUFFIX;
        TypeSpec.Builder typeSpecBuilder = TypeSpec.interfaceBuilder(className)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(BaseRepository.class),
                        ClassName.get(typeElement), ClassName.get(Long.class)))
                .addModifiers(Modifier.PUBLIC);

        DefaultNameContext nameContext = getNameContext(typeElement);
        String packageName = nameContext.getRepositoryPackageName();
        genJavaSourceFile(packageName, typeElement.getAnnotation(GenRepository.class).sourcePath(), typeSpecBuilder);
    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return GenRepository.class;
    }

    @Override
    public String generatePackage(TypeElement typeElement) {
        return typeElement.getAnnotation(GenRepository.class).pkgName();
    }
}
