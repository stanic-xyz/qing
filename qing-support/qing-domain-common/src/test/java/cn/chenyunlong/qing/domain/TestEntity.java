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

import cn.chenyunlong.qing.domain.common.BaseSimpleBusinessEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 测试实体类
 *
 * @author 陈云龙
 * @since 2024-08-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TestEntity extends BaseSimpleBusinessEntity<TestIdentifiable> {

    /**
     * 实体名称
     */
    private String name;

    /**
     * 实体描述
     */
    private String description;

    /**
     * 实体状态
     */
    private String status;

    /**
     * 默认构造函数
     */
    public TestEntity() {
        super();
    }

    /**
     * 带参数的构造函数
     *
     * @param id   实体ID
     * @param name 实体名称
     */
    public TestEntity(TestIdentifiable id, String name) {
        super();
        this.setId(id);
        this.name = name;
        this.init();
    }

    /**
     * 带参数的构造函数
     *
     * @param name        实体名称
     * @param description 实体描述
     */
    public TestEntity(String name, String description) {
        super();
        this.name = name;
        this.description = description;
        this.status = "ACTIVE";
        this.init();
    }

    /**
     * 激活实体
     */
    public void activate() {
        this.status = "ACTIVE";
    }

    /**
     * 停用实体
     */
    public void deactivate() {
        this.status = "INACTIVE";
    }

    /**
     * 检查实体是否激活
     *
     * @return 如果激活返回true，否则返回false
     */
    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }
}
