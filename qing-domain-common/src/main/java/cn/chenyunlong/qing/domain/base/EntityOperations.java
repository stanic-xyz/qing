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


import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

/**
 * 实体类型操作。
 *
 * @author gim 2022/3/5 9:52 下午
 */
public abstract class EntityOperations {

    public static <T extends BaseAggregate, ID extends AggregateId> EntityUpdater<T, ID> doUpdate(BaseRepository<T, ID> repository) {
        return new EntityUpdater<>(repository);
    }

    public static <T extends BaseAggregate, ID extends AggregateId> EntityCreator<T, ID> doCreate(BaseRepository<T, ID> repository) {
        return new EntityCreator<>(repository);
    }


}
