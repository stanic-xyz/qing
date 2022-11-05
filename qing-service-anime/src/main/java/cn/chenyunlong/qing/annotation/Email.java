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

package cn.chenyunlong.qing.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Email {

    /**
     * 接收人
     *
     * @return {@link String}
     */
    String receiver();

    /**
     * 内容
     *
     * @return {@link String}
     */
    String content();

    /**
     * 主题
     *
     * @return {@link String}
     */
    String object();

    /**
     * 模板ID
     *
     * @return int
     */
    int templateId() default 1;

    /**
     * 电子邮件类型 1：预约信息，2，收购数据，3：预约语音
     *
     * @return int
     */
    int emailType() default 1;
}
