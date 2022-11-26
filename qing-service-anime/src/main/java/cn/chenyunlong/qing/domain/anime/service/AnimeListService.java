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

import cn.chenyunlong.qing.domain.anime.PlaylistEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Stan
 */
public interface AnimeListService extends IService<PlaylistEntity> {

    /**
     * 根据动漫ID获取播放列表
     *
     * @param animeId 动漫ID
     * @return 播放列表
     */
    List<PlaylistEntity> getAnimePlayList(Long animeId);
}
