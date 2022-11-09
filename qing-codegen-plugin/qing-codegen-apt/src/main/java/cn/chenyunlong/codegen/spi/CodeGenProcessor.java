/*
 * Copyright (c) 2019-2022 YunLong Chen
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

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;

/**
 * @author gim
 */
public interface CodeGenProcessor {

    /**
     * 需要解析的类上的注解
     *
     * @return {@link Class}<{@link ?} {@link extends} {@link Annotation}>
     */
    Class<? extends Annotation> getAnnotation();

    /**
     * 获取生成的包路径
     *
     * @param typeElement 类型元素
     * @return {@link String}
     */
    String generatePackage(TypeElement typeElement);

    /**
     * 代码生成逻辑
     *
     * @param typeElement      类型元素
     * @param roundEnvironment 周围环境
     * @throws Exception 异常
     */
    void generate(TypeElement typeElement, RoundEnvironment roundEnvironment) throws Exception;
}
