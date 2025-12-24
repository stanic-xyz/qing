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

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * 加载器。
 *
 * @author gim 2022/1/28 9:07 下午
 */
public interface Loader<T> {

    /**
     * 根据ID加载数据，生成数据更新处理器。
     *
     * @param id 数据ID
     * @return 更新执行器
     */
    UpdateHandler<T> loadById(Serializable id);

    /**
     * 加载器。
     *
     * @param supplier 数据处理器提供者
     * @return 数据加载执行处理器
     */
    UpdateHandler<T> load(Supplier<T> supplier);

}
