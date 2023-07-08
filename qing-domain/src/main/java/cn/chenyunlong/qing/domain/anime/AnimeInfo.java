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

package cn.chenyunlong.qing.domain.anime;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.jpa.support.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @author Stan
 */

@Getter
@Setter
@ToString
@Entity
@Table(name = "anime_info")
public class AnimeInfo extends BaseEntity {

    @FieldDesc(description = "动漫名称")
    private String name;

    @FieldDesc(description = "介绍信息")
    private String instruction;

    @Schema
    @FieldDesc(description = "区域Id")
    private Long districtId;

    @FieldDesc(description = "区域名称")
    private String districtName;

    @FieldDesc(description = "封面")
    private String coverUrl;

    @FieldDesc(description = "类型信息")
    private Long typeId;

    @FieldDesc(description = "类型名称")
    private String typeName;

    @FieldDesc(description = "原始名称")
    private String originalName;

    @FieldDesc(description = "其他名称")
    private String otherName;

    @FieldDesc(description = "作者")
    private String author;

    @FieldDesc(description = "公司")
    private String company;

    @FieldDesc(description = "发布日期")
    private LocalDate premiereDate;

    @FieldDesc(description = "播放状态")
    private PlayStatus playStatus;

    @FieldDesc(description = "动漫类型")
    private String plotType;

    @FieldDesc(description = "动漫标签列表")
    private String tags;

    @FieldDesc(description = "官网")
    private String officialWebsite;

    @FieldDesc(description = "播放热度")
    private String playHeat;

    @FieldDesc(description = "排序号")
    private Integer orderNo;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AnimeInfo animeInfo = (AnimeInfo) o;
        return getId() != null && Objects.equals(getId(), animeInfo.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
