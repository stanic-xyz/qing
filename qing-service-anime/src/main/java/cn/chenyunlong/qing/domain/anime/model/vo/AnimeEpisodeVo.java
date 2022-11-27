/*
 * Copyright (c) 2019-2022 YunLong Chen
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

package cn.chenyunlong.qing.domain.anime.model.vo;

import cn.chenyunlong.qing.domain.anime.AnimeEpisodeEntity;
import cn.chenyunlong.qing.infrastructure.model.dto.base.OutputConverter;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnimeEpisodeVo implements OutputConverter<AnimeEpisodeVo, AnimeEpisodeEntity> {
    /**
     * 视频ID
     */
    private Long id;

    /**
     * 动漫ID
     */
    private Long animeId;
    private Long playlistId;
    private String name;

    /**
     * 视频状态，0正常
     */
    private Integer status;

    /**
     * 上传者名称
     */
    private String uploadderName;

    /**
     * 上传用户ID
     */
    private Long uploaderId;

    /**
     * 视频上传时间
     */
    private LocalDateTime uploadTime;

    /**
     * 视频地址
     */
    private String url1;

    /**
     * 视频播放地址3
     */
    private String url3;

    /**
     * 视频播放地址2
     */
    private String url2;

    /**
     * 视频排序
     */
    private Integer orderNo;
}
