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

package person.pluto.natcross2.common;

/**
 * <p>
 * 操作对象，jdk8的不带重设功能，但是我们需要，所以进行了类似的重写
 * </p>
 *
 * @param <T> 存放的类型
 * @author Pluto
 * @since 2020-01-08 16:35:46
 */
public class Optional<T> {

    public static <T> Optional<T> of(T value) {
        return new Optional<T>(value);
    }

    public Optional(T value) {
        this.value = value;
    }

    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

}
