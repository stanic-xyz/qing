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

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 实体操作
 *
 * @author gim
 * @since 2022/11/14
 */
public abstract class EntityOperations {

    /**
     * 更新对象
     *
     * @param baseMapper 基地映射器
     * @return {@link EntityUpdater}<{@link T}>
     */
    public static <T> EntityUpdater<T> doUpdate(BaseMapper<T> baseMapper) {
        return new EntityUpdater<>(baseMapper);
    }

    /**
     * 产生对象
     *
     * @param baseMapper 基地映射器
     * @return {@link EntityCreator}<{@link T}>
     */
    public static <T> EntityCreator<T> doCreate(BaseMapper<T> baseMapper) {
        return new EntityCreator<>(baseMapper);
    }


}
