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

import cn.chenyunlong.codegen.annotation.base.BaseGen;

import java.lang.annotation.*;

/**
 * 生成查询请求体
 *
 * @author 陈云龙
 * @apiNote 标记为该注解，可以自动生成查询请求体
 * @since 2019-10-08 17:13
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
@BaseGen
public @interface GenQuery {

    /**
     * 代码生成位置（包名）。
     *
     * @return 包名
     */
    String pkgName() default "dto.query";

    /**
     * 默认代码生成路径
     */
    String sourcePath() default "src/main/java";

    /**
     * 重新运行是是否覆盖原有的源文件，重新生成源代码。
     */
    boolean overrideSource() default false;
}
