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

package cn.chenyunlong.mybatis.support;

import java.util.function.Supplier;

/**
 * 创建器
 *
 * @author gim 2022/1/28 9:55 下午
 * @since 2022/11/14
 */
public interface Create<T> {

    /**
     * 创建
     *
     * @param supplier 创建的生成器
     * @return {@link UpdateHandler}<{@link T}>
     */
    UpdateHandler<T> create(Supplier<T> supplier);

}
