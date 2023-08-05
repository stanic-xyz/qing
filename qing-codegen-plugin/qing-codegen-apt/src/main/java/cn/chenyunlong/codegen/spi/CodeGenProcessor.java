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

package cn.chenyunlong.codegen.spi;

import java.lang.annotation.Annotation;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

/**
 * 代码生成器。
 *
 * @author chenyunlong
 */
public interface CodeGenProcessor {

    /**
     * 支持方法。
     *
     * @param typeElement      支持方法
     * @param roundEnvironment 周围环境
     * @return 是否支持处理该方法
     */
    boolean support(TypeElement typeElement, RoundEnvironment roundEnvironment);

    /**
     * 生成类。
     *
     * @param typeElement 顶层元素
     * @param roundEnv    周围环境
     * @param useLombok   是否使用lombok
     */
    void generateClass(TypeElement typeElement, RoundEnvironment roundEnv,
                       boolean useLombok);

    /**
     * 获取支持注释。
     *
     * @return {@link Class}<{@link ?} {@link extends} {@link Annotation}>
     */
    Class<? extends Annotation> getSupportedAnnotation();

    /**
     * 是否重写文件。
     *
     * @return true，重写文件，false不支持重写
     */
    boolean overwrite();

    /**
     * 初始化。
     *
     * @param processingEnvironment 处理环境
     */
    void init(ProcessingEnvironment processingEnvironment);


    /**
     * 获取领域对象名称。
     *
     * @param typeElement 处理的对象名称
     * @return 领域对象名称
     */
    Name getDomainName(TypeElement typeElement);

    /**
     * 获取生成的文件package。
     *
     * @return 生成的文件package
     */
    String getBasePackageName(TypeElement typeElement);

    /**
     * 获取子包名称。
     *
     * @param typeElement 类型元素
     * @return 生成的文件package
     */
    String getSubPackageName(TypeElement typeElement);

    /**
     * 获取源文件路径。
     *
     * @param typeElement 元素类型
     * @return 元素类型生成路径
     */
    String getSourcePath(TypeElement typeElement);

    /**
     * 执行至的执行顺序。
     *
     * @return 执行器执行顺序
     */
    default int getOrder() {
        return Integer.MIN_VALUE;
    }
}
