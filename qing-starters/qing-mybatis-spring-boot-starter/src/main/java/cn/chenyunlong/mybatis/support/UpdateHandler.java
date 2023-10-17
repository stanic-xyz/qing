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

import java.util.function.Consumer;

/**
 * 更新处理程序。
 *
 * @author gim 2022/1/28 9:10 下午
 * @since 2022/11/14
 */
public interface UpdateHandler<T> {

    /**
     * 更新。
     *
     * @param consumer 消费者
     * @return {@link Executor}<{@link T}>
     */
    Executor<T> update(Consumer<T> consumer);

}
