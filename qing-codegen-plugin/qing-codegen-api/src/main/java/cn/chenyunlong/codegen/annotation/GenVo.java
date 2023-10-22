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
 * 生成VO
 *
 * @author 陈云龙
 * @author Gim
 * @since 2022/11/06
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
@BaseGen
public @interface GenVo {

    /**
     * 代码生成位置（包名）。
     *
     * @return 包名
     */
    String pkgName() default "vo";

    /**
     * 源路径
     *
     * @return {@link String}
     */
    String sourcePath() default "src/main/java";

    /**
     * 是否覆盖源
     *
     * @return boolean
     */
    boolean overrideSource() default false;

    /**
     * jpa
     *
     * @return boolean
     */
    boolean jpa() default true;
}
