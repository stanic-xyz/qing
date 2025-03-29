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

package cn.chenyunlong.qing.domain.base;

import java.util.function.Supplier;

/**
 * 加载器。
 *
 * @author gim 2022/1/28 9:07 下午
 * @since 2022/10/24
 */
public interface Loader<T, ID> {

    /**
     * 通过id加载对象。
     *
     * @param id id
     * @return {@link UpdateHandler}<{@link T}>
     */
    UpdateHandler<T> loadById(ID id);

    /**
     * 执行加载操作。
     *
     * @param t t
     * @return {@link UpdateHandler}<{@link T}>
     */
    UpdateHandler<T> load(Supplier<T> t);

}
