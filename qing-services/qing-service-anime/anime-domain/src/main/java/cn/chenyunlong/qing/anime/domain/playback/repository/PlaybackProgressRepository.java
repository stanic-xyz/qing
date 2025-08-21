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

package cn.chenyunlong.qing.anime.domain.playback.repository;

import cn.chenyunlong.qing.anime.domain.playback.PlaybackProgress;

import java.util.List;
import java.util.Optional;

/**
 * 播放进度仓储接口
 *
 * @author 陈云龙
 */
public interface PlaybackProgressRepository {

    /**
     * 保存播放进度
     */
    PlaybackProgress save(PlaybackProgress progress);

    /**
     * 根据用户ID和剧集ID查找播放进度
     */
    Optional<PlaybackProgress> findByUserIdAndEpisodeId(Long userId, Long episodeId);

    /**
     * 根据用户ID和动漫ID查找播放进度列表
     */
    List<PlaybackProgress> findByUserIdAndAnimeId(Long userId, Long animeId);

    /**
     * 根据用户ID查找所有播放进度
     */
    List<PlaybackProgress> findByUserId(Long userId);

    /**
     * 根据用户ID查找最近观看的播放进度
     */
    List<PlaybackProgress> findRecentByUserId(Long userId, int limit);

    /**
     * 根据用户ID查找已完成观看的播放进度
     */
    List<PlaybackProgress> findCompletedByUserId(Long userId);

    /**
     * 删除播放进度
     */
    void deleteById(Long id);

    /**
     * 根据用户ID和动漫ID删除播放进度
     */
    void deleteByUserIdAndAnimeId(Long userId, Long animeId);
}