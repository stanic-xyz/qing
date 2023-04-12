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

package cn.chenyunlong.codegen.spi;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;

/**
 * @author chenyunlong
 */
public interface CodeGenProcessor {

    /**
     * 支持方法
     *
     * @param annotationClassName 支持方法
     * @return 是否支持处理该方法
     */
    boolean support(String annotationClassName);

    /**
     * 生成类
     * 生成Class
     *
     * @param typeElement      顶层元素
     * @param roundEnvironment 周围环境
     * @param useLombok        是否使用lombok
     */
    void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment, boolean useLombok);

    /**
     * 获取支持注释
     *
     * @return {@link Class}<{@link ?} {@link extends} {@link Annotation}>
     */
    Class<? extends Annotation> getSupportedAnnotation();

    /**
     * 是否重写文件
     *
     * @return true，重写文件，false不支持重写
     */
    boolean overwrite();

    /**
     * 初始化
     *
     * @param processingEnvironment 处理环境
     */
    void init(ProcessingEnvironment processingEnvironment);

}
