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

package cn.chenyunlong.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 字段描述信息
 *
 * @author : Gim
 * @since : 2019/11/25 16:20
 */
@Target(ElementType.FIELD)
public @interface FieldDesc {
    /**
     * 字段名称
     *
     * @return {@link String}
     */
    String name() default "";

    /**
     * 描述
     *
     * @return {@link String}
     */
    String description() default "";
}
