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

package cn.chenyunlong.qing.anime.domain.episode;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import lombok.Getter;
import lombok.Setter;

/**
 * 单集
 *
 * @author 陈云龙
 */
@Getter
@Setter
public class Episode extends BaseAggregate<EpisodeId> {

    @FieldDesc(name = "名称")
    private String name;

    @FieldDesc(name = "动漫ID")
    private Long animeId;

    @FieldDesc(name = "播放源ID")
    private Long playListId;

    @FieldDesc(name = "描述信息")
    private String description;

    @FieldDesc(name = "播放地址")
    private String playUrl;

    @FieldDesc(name = "集数", description = "集数")
    private Integer episodeNumber;
}
