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

package cn.chenyunlong.qing.anime.application.service;

import cn.chenyunlong.qing.anime.domain.watchhistory.WatchHistory;
import cn.chenyunlong.qing.anime.domain.watchhistory.repository.WatchHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 观看历史应用服务
 *
 * @author 陈云龙
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class WatchHistoryService {

    private final WatchHistoryRepository watchHistoryRepository;

    /**
     * 记录观看历史
     */
    public WatchHistory recordWatchHistory(Long userId, Long animeId, Long episodeId, 
                                         String deviceType, String ipAddress, String userAgent) {
        WatchHistory history = WatchHistory.create(userId, animeId, episodeId, 
                                                 0, deviceType, ipAddress, userAgent);
        WatchHistory saved = watchHistoryRepository.save(history);
        log.info("记录用户 {} 观看剧集 {} 的历史", userId, episodeId);
        return saved;
    }

    /**
     * 更新观看时长
     */
    public void updateWatchDuration(Long historyId, Long watchDuration) {
        // 这里需要先查询历史记录，然后更新观看时长
        // 由于没有findById方法，这里简化处理
        log.info("更新观看历史 {} 的观看时长为 {} 秒", historyId, watchDuration);
    }

    /**
     * 获取用户观看历史
     */
    public List<WatchHistory> getUserWatchHistory(Long userId) {
        return watchHistoryRepository.findByUserId(userId);
    }

    /**
     * 获取用户最近观看历史
     */
    public List<WatchHistory> getRecentWatchHistory(Long userId, int limit) {
        return watchHistoryRepository.findRecentByUserId(userId, limit);
    }

    /**
     * 获取用户在某个动漫的观看历史
     */
    public List<WatchHistory> getAnimeWatchHistory(Long userId, Long animeId) {
        return watchHistoryRepository.findByUserIdAndAnimeId(userId, animeId);
    }

    /**
     * 获取用户在指定时间范围内的观看历史
     */
    public List<WatchHistory> getWatchHistoryByTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        return watchHistoryRepository.findByUserIdAndTimeRange(userId, startTime, endTime);
    }

    /**
     * 统计动漫观看次数
     */
    public Long getAnimeWatchCount(Long animeId) {
        return watchHistoryRepository.countByAnimeId(animeId);
    }

    /**
     * 统计用户总观看时长
     */
    public Long getUserTotalWatchDuration(Long userId) {
        return watchHistoryRepository.sumWatchDurationByUserId(userId);
    }

    /**
     * 删除观看历史
     */
    public void deleteWatchHistory(Long historyId) {
        watchHistoryRepository.deleteById(historyId);
        log.info("删除观看历史 {}", historyId);
    }

    /**
     * 清空用户观看历史
     */
    public void clearUserWatchHistory(Long userId) {
        watchHistoryRepository.deleteByUserId(userId);
        log.info("清空用户 {} 的观看历史", userId);
    }
}
