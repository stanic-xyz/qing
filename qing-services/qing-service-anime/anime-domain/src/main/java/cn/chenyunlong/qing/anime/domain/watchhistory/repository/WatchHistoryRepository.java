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

package cn.chenyunlong.qing.anime.domain.watchhistory.repository;

import cn.chenyunlong.qing.anime.domain.watchhistory.WatchHistory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 观看历史仓储接口
 *
 * @author 陈云龙
 */
public interface WatchHistoryRepository {

    /**
     * 保存观看历史
     */
    WatchHistory save(WatchHistory history);

    /**
     * 根据用户ID查找观看历史
     */
    List<WatchHistory> findByUserId(Long userId);

    /**
     * 根据用户ID和时间范围查找观看历史
     */
    List<WatchHistory> findByUserIdAndTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据用户ID查找最近的观看历史
     */
    List<WatchHistory> findRecentByUserId(Long userId, int limit);

    /**
     * 根据用户ID和动漫ID查找观看历史
     */
    List<WatchHistory> findByUserIdAndAnimeId(Long userId, Long animeId);

    /**
     * 根据动漫ID统计观看次数
     */
    Long countByAnimeId(Long animeId);

    /**
     * 根据用户ID统计总观看时长
     */
    Long sumWatchDurationByUserId(Long userId);

    /**
     * 删除观看历史
     */
    void deleteById(Long id);

    /**
     * 根据用户ID删除观看历史
     */
    void deleteByUserId(Long userId);
}
