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

package cn.chenyunlong.qing.domain.entity;


import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.qing.domain.infrustructure.converter.EntityTypeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 实体信息。
 *
 * @author 陈云龙
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Entity
@Table(name = "entity")
public class Entity extends BaseJpaAggregate {

    @FieldDesc(name = "用户名", description = "用户（唯一），用于前端显示！")
    private String name;

    @Convert(converter = EntityTypeConverter.class)
    @FieldDesc(name = "实体类型")
    private EntityType entityType;

    @FieldDesc(name = "点赞数量")
    private Long zanCount;

    @Override
    public void init() {
        super.init();
    }
}
