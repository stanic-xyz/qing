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
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author gim
 */
public final class StringUtils {

    private StringUtils() {
    }

    public static String camel(String source) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, source);
    }

    /**
     * 包含空
     *
     * @param list string数组
     * @return boolean
     */
    public static boolean containsNull(String... list) {
        List<String> temp = Lists.newArrayList();
        Collections.addAll(temp, list);
        List<String> nullList = temp.stream().filter(Objects::isNull).toList();
        return nullList.size() > 0;
    }

    /**
     * 判断字符串不为空
     *
     * @param str 字符串
     * @return 字符串是否为空
     */
    public static boolean isNotBlank(String str) {
        return org.springframework.util.StringUtils.hasText(str);
    }
}
