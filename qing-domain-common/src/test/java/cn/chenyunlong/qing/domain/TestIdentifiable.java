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

import cn.chenyunlong.qing.domain.common.Identifiable;

import java.util.UUID;

/**
 * 测试实体ID类
 *
 * @author 陈云龙
 * @since 2024-08-24
 */
public record TestIdentifiable(String value) implements Identifiable<String> {

    @Override
    public String id() {
        return value;
    }

    /**
     * 构造函数
     *
     * @param value ID值
     */
    public TestIdentifiable {
    }

    /**
     * 创建新的ID
     *
     * @return 新的TestEntityId实例
     */
    public static TestIdentifiable newId() {
        return new TestIdentifiable(UUID.randomUUID().toString());
    }

    /**
     * 从字符串创建ID
     *
     * @param value 字符串值
     * @return TestEntityId实例
     */
    public static TestIdentifiable of(String value) {
        return new TestIdentifiable(value);
    }

    @Override
    public String toString() {
        return "TestEntityId{" +
                "value='" + value + '\'' +
                '}';
    }
}
