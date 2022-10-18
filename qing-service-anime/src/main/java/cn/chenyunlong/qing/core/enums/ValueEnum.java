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

package cn.chenyunlong.qing.core.enums;

import org.springframework.util.Assert;

import java.util.stream.Stream;

/**
 * Interface for value enum.
 *
 * @param <T> value type
 * @author johnniang
 */
public interface ValueEnum<T> {

    /**
     * Converts value to corresponding enum.
     *
     * @param enumType enum type
     * @param value    database value
     * @param <V>      value generic
     * @param <E>      enum generic
     * @return corresponding enum
     */
    static <V, E extends ValueEnum<V>> E valueToEnum(Class<E> enumType, V value) {
        Assert.notNull(enumType, "enum type must not be null");
        Assert.notNull(value, "value must not be null");
        Assert.isTrue(enumType.isEnum(), "type must be an enum type");

        return Stream.of(enumType.getEnumConstants())
                .filter(item -> item.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("unknown database value: " + value));
    }

    /**
     * Gets enum value.
     *
     * @return enum value
     */
    T getValue();

}
