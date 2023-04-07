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

import cn.chenyunlong.codegen.annotation.SupportedGenTypes;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author chenyunlong
 */
public abstract class CodeGenProcessor extends AbstractProcessor {


    /**
     * 支持方法
     *
     * @param annotationClassName 支持方法
     * @return 是否支持处理该方法
     */
    public boolean support(String annotationClassName) {
        SupportedGenTypes supportedGenTypes = this.getClass().getAnnotation(SupportedGenTypes.class);
        return supportedGenTypes != null && supportedGenTypes.types().getName().equals(annotationClassName);

    }

    /**
     * 生成类
     * 生成Class
     *
     * @param typeElement      顶层元素
     * @param roundEnvironment 周围环境
     * @param useLombok        是否使用lombok
     */
    public void generateClass(TypeElement typeElement, RoundEnvironment roundEnvironment, boolean useLombok) {

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }


    public Class<? extends Annotation> getSupportedAnnotation() {
        SupportedGenTypes supportedGenTypes = this.getClass().getAnnotation(SupportedGenTypes.class);
        if (supportedGenTypes != null) {
            return supportedGenTypes.types();
        }
        return null;
    }
}
