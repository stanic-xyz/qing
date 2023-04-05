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
import cn.chenyunlong.jpa.support.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

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
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "anime_episode")
public class Episode extends BaseEntity {

    private Long animeId;

    private Long listId;

    /**
     * 视频标题名称
     */
    private String name;

    /**
     * 视频状态，0正常
     */
    private Integer status;

    /**
     * 上传者名称
     */
    private String uploaderName;

    /**
     * 上传用户ID
     */
    private Long uploaderId;

    /**
     * 视频地址
     */
    private String url;

    /**
     * 视频排序
     */
    private Integer orderNo = 0;
}
