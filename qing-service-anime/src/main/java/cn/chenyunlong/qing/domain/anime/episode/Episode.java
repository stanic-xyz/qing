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

package cn.chenyunlong.qing.domain.anime.episode;

import cn.chenyunlong.codegen.annotation.*;
import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.jpa.support.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * anime_episode
 *
 * @author Stan
 * @since 2022/10/18
 */

@GenBase(basePackage = "cn.chenyunlong.qing.domain.anime.episode")
@GenVo
@GenCreator
@GenUpdater
@GenRepository
@GenController
@GenService
@GenServiceImpl
@GenQuery
@GenCreateRequest
@GenUpdateRequest
@GenQueryRequest
@GenResponse
@GenFeign
@GenMapper
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "anime_episode")
public class Episode extends BaseEntity {

    @FieldDesc(name = "动漫ID")
    private Long animeId;

    @FieldDesc(name = "播放列表ID")
    private Long listId;

    /**
     * 视频标题名称
     */
    @FieldDesc(name = "视频标题名称")
    private String name;

    /**
     * 视频状态，0正常
     */
    @FieldDesc(name = "视频状态")
    private Integer status;

    /**
     * 上传者名称
     */
    @FieldDesc(name = "上传者名称")
    private String uploaderName;

    /**
     * 上传用户ID
     */
    @FieldDesc(name = "上传用户ID")
    private Long uploaderId;

    /**
     * 视频地址
     */
    @FieldDesc(name = "视频地址")
    private String url;

    /**
     * 视频排序
     */
    @FieldDesc(name = "视频排序")
    private Integer orderNo = 0;
}
