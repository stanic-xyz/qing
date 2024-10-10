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

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 单集
 *
 * @author 陈云龙
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "anime_play_source")
public class PlaySource extends BaseJpaAggregate {

    @FieldDesc(name = "名称")
    private String name;

    @FieldDesc(name = "动漫ID")
    private Long animeId;

    @FieldDesc(name = "播放源ID")
    private Long collectionId;

    @FieldDesc(name = "播放源名称")
    private String collectionName;

    @FieldDesc(name = "播放源图标")
    private String icon;

    @OneToMany(targetEntity = Episode.class, fetch = jakarta.persistence.FetchType.LAZY)
    private List<Episode> episodes;
}
