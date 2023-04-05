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

package cn.chenyunlong.codegen.annotation;


import cn.chenyunlong.codegen.annotation.enumes.OrmType;

import java.lang.annotation.Inherited;

/**
 * 代码生成器基础文件
 *
 * @author Stan
 * @since 2022/11/27
 */

@Inherited
public @interface GenBase {


    /**
     * 代码生成器根包
     *
     * @return {@link String}
     */
    String basePackage() default "";


    /**
     * 源代码路径
     *
     * @return {@link String}
     */
    String sourcePath() default "src/main/java";

    /**
     * orm框架类型
     *
     * @return {@link OrmType}
     */
    OrmType ormType() default OrmType.MYBATIS_PLUS;

}
