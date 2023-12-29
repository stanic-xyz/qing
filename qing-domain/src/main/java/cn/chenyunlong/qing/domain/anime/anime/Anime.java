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

package cn.chenyunlong.qing.domain.anime.anime;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.qing.infrustructure.converter.PlayStatusConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 动漫信息。
 *
 * @author 陈云龙
 */

@Getter
@Setter
@ToString
@Entity
@Table(name = "anime")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Anime extends BaseJpaAggregate {

    @FieldDesc(description = "名称")
    private String name;

    @FieldDesc(description = "介绍")
    private String instruction;

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
    private Long companyId;

    @FieldDesc(description = "公司名称")
    private String companyName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FieldDesc(description = "发布日期")
    private LocalDate premiereDate;

    @Convert(converter = PlayStatusConverter.class)
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
}
