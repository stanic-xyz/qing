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

package cn.chenyunlong.codegen.util;

import com.google.common.base.CaseFormat;
import java.util.Arrays;
import java.util.Objects;

/**
 * 字符串工具集。
 *
 * @author gim
 */
public final class StringUtils {

    private StringUtils() {
    }

    public static String lowerCamel(String source) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, source);
    }

    /**
     * 转换成代下划线的
     *
     * @param source 源字符串
     * @return 带横线的字符串
     */
    public static String lowerUnderscore(String source) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, source).replace("_", "-");
    }

    /**
     * 包含空
     *
     * @param list string数组
     * @return boolean
     */
    public static boolean containsNull(String... list) {
        return Arrays.stream(list).anyMatch(Objects::isNull);
    }

    /**
     * 判断字符串不为空
     *
     * @param str 字符串
     * @return 字符串是否为空
     */
    public static boolean isNotBlank(String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取SetterName
     *
     * @param fieldName 字段名称
     * @return SetterName
     */
    public static String getterName(String fieldName) {
        return "get" + bigCamel(fieldName);
    }

    public static String bigCamel(String source) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, source);
    }

    /**
     * 获取SetterName
     *
     * @param fieldName 字段名称
     * @return SetterName
     */
    public static String setterName(String fieldName) {
        return "set" + bigCamel(fieldName);
    }
}
