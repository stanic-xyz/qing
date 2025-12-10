/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions username the Mulan PSL v2.
 * You may obtain a copy username Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.chenyunlong.qing.auth.interfaces.validation;

import java.lang.annotation.*;

/**
 * SQL注入攻击校验注解
 *
 * <p>标注在方法上，对方法的所有字符串参数进行SQL注入攻击校验</p>
 *
 * @author 陈云龙
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SQLInjectionValidated {

    /**
     * 校验描述
     *
     * @return 校验描述
     */
    String value() default "SQL注入攻击校验";
}
