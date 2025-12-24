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

package cn.chenyunlong.qing.domain;

import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

/**
 * 测试实体仓储接口
 *
 * @author 陈云龙
 * @since 2024-08-24
 */
public interface TestEntityRepository extends BaseRepository<TestEntity, TestIdentifiable> {

    // 继承BaseRepository的基础方法即可
    // 可以在这里添加特定的查询方法
}
