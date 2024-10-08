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

package cn.chenyunlong.common.constants;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Objects;

/**
 * 枚举基础类。
 *
 * @param <V> 数值类型
 */
public interface BaseEnum<V> {

    /**
     * 根据Code解析枚举。
     *
     * @param code  枚举代码
     * @param clazz 枚举枚举类型
     * @param <T>   枚举类型的泛型
     * @return 枚举
     */
    static <T extends BaseEnum<V>, V> T parseByCode(V code, Class<T> clazz) {
        T[] enumConstants = clazz.getEnumConstants();
        return Arrays
            .stream(enumConstants)
            .filter(baseEnum -> Objects.equals(baseEnum.getValue(), code))
            .findFirst()
            .orElse(null);
    }

    /**
     * 获取code码存入数据库。
     *
     * @return 获取编码
     */
    @JsonValue
    V getValue();

    /**
     * 获取编码名称，便于维护。
     *
     * @return 获取编码名称
     */
    String getName();
}
