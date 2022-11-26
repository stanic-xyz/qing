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

package cn.chenyunlong.qing.domain.anime.service;

import cn.chenyunlong.qing.infrastructure.model.dto.PlayListDTO;

import java.util.List;

/**
 * 播放合集服务接口
 */
public interface PlaylistService {
    /**
     * 根据动漫信息获取动漫的播放列表
     *
     * @param animeId 动漫ID
     * @return 播放列表
     */
    List<PlayListDTO> listPlayListBy(Long animeId);

    /**
     * 获取播放列表
     *
     * @param playlistId 播放列表ID
     * @return 播放列表信息
     */
    PlayListDTO getById(Long playlistId);
}
