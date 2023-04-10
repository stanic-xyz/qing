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

package cn.chenyunlong.qing.domain.anime.anime;

import cn.chenyunlong.codegen.annotation.*;
import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.jpa.support.domain.BaseEntity;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.AnimeInfoBizInfo;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.model.AnimeInfoRecommendBizInfo;
import cn.chenyunlong.qing.domain.anime.anime.events.AnimeInfoInEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;

/**
 * @author Stan
 */

@GenBase(basePackage = "cn.chenyunlong.qing.domain.anime.anime")
@GenVo
@GenCreator
@GenUpdater
@GenRepository
@GenService
@GenServiceImpl
@GenController(name = "动漫信息")
@GenQuery
@GenCreateRequest
@GenUpdateRequest
@GenQueryRequest
@GenResponse
@GenFeign
@GenMapper
@Data
@Entity
@Table(name = "anime_info")
@EqualsAndHashCode(callSuper = true)
public class AnimeInfo extends BaseEntity {

    @FieldDesc(description = "动漫名称")
    private String name;

    @FieldDesc(description = "介绍信息")
    private String instruction;

    @FieldDesc(description = "区域Id")
    private Long districtId;

    @FieldDesc(description = "区域名称")
    private String districtName;
    private String coverUrl;
    private Long typeId;
    private String typeName;
    private String originalName;
    private String otherName;
    private String author;
    private String company;
    private LocalDate premiereDate;
    private PlayStatus playStatus;
    private String plotType;
    private String tags;
    private String officialWebsite;
    private String playHeat;
    private Integer orderNo;

    /**
     * 导入动漫
     */
    public void in(AnimeInfoBizInfo bizInfo) {
        if (Objects.equals(ValidStatus.VALID, this.getValidStatus())) {
            throw new BusinessException(AnimeInfoErrorCode.ASSET_HAS_IN);
        }
        setValidStatus(ValidStatus.VALID);
        registerEvent(new AnimeInfoInEvent(this, bizInfo));
    }

    /**
     * 添加到推荐
     *
     * @param recommendBizInfo 推荐信息
     */
    public void recommend(AnimeInfoRecommendBizInfo recommendBizInfo) {

    }
}
