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

package cn.chenyunlong.qing.infrastructure.cache.lock;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Cache lock annotation.
 *
 * @author johnniang
 * @date 3/28/19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheLock {

    /**
     * Cache prefix, default is ""
     *
     * @return cache prefix
     */
    @AliasFor("value") String prefix() default "";

    /**
     * Alias of prefix, default is ""
     *
     * @return alias of prefix
     */
    @AliasFor("prefix") String value() default "";

    /**
     * Expired time, default is 5.
     *
     * @return expired time
     */
    long expired() default 5;

    /**
     * Time unit, default is TimeUnit.SECONDS.
     *
     * @return time unit
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * Delimiter, default is ':'
     *
     * @return delimiter
     */
    String delimiter() default ":";

    /**
     * Whether delete cache after method invocation.
     *
     * @return true if delete cache after method invocation; false otherwise
     */
    boolean autoDelete() default true;

    /**
     * Whether trace the request info.
     *
     * @return true if trace the request info; false otherwise
     */
    boolean traceRequest() default false;
}
