/*
 * Copyright (c) 2023  YunLong Chen
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

package cn.chenyunlong.codegen.annotation;

import cn.chenyunlong.codegen.cache.CacheStrategy;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface SupportedGenTypes {


    /**
     * 支持的方法
     */
    Class<? extends Annotation> types();

    /**
     * 支持的类型
     *
     * @return 类型信息
     */
    String annotationName() default "";

    /**
     * 是否默认覆盖
     *
     * @return 是否默认覆盖
     */
    boolean override() default false;

    /**
     * 缓存策略
     * 定义文件生成的缓存行为，优化编译性能
     *
     * @return 缓存策略
     */
    CacheStrategy cacheStrategy() default CacheStrategy.NONE;
}
