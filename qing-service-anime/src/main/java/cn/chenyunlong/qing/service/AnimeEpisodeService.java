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

package cn.chenyunlong.qing.service;

import cn.chenyunlong.qing.domain.anime.AnimeEpisodeEntity;
import cn.chenyunlong.qing.model.dto.AnimeEpisodeDTO;
import cn.chenyunlong.qing.model.params.AddEpisodeParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Stan
 */
public interface AnimeEpisodeService extends IService<AnimeEpisodeEntity> {

    /**
     * 通过动漫ID查询动漫内部的所有播放信息
     *
     * @param animeId 动漫ID
     * @return 动漫的单集信息集合
     */
    List<AnimeEpisodeDTO> listEpisodeByAnimeId(Long animeId);

    /**
     * 视频信息
     *
     * @param episodeParam 添加视频信息
     * @return 动漫信息
     */
    AnimeEpisodeDTO add(AddEpisodeParam episodeParam);

    /**
     * 根据动漫ID获取播放视频列表
     *
     * @param animeId 动漫ID
     * @return 播放列表
     */
    List<AnimeEpisodeEntity> getByAnimeId(Long animeId);
}
