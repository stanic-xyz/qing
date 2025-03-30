/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.jpa.support.BaseJpaEntity;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 动漫信息。
 *
 * @author 陈云龙
 */


@Getter
@Setter
@ToString
@Entity
@Table(name = "anime_play_list")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class PlayListEntity extends BaseJpaEntity {

    @FieldDesc(description = "动漫ID")
    private Long animeId;

    @FieldDesc(description = "名称")
    private String name;

    @FieldDesc(description = "描述")
    private String description;

    @FieldDesc(description = "封面图标")
    private String icon;
}
