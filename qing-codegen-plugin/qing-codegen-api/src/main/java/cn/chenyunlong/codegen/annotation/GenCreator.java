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
 * 生产创建器。
 *
 * @author : Gim
 * @apiNote : 实现这个代码可以自动生成创建器
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
@BaseGen
public @interface GenCreator {

    /**
     * 代码生成位置（包名）。
     *
     * @return 包名
     */
    String pkgName() default
        "dto.creator";

    /**
     * 生成源代码的目录。
     *
     * @return 源代码生成目录
     */
    String sourcePath() default "src/main/java";

    /**
     * 是否需要覆盖源文件。
     *
     * @return 是否覆盖源文件
     */
    boolean overrideSource() default false;
}
