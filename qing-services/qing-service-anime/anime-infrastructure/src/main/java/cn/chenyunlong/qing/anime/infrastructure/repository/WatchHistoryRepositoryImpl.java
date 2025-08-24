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

package cn.chenyunlong.qing.anime.infrastructure.repository;

import cn.chenyunlong.qing.anime.domain.watchhistory.WatchHistory;
import cn.chenyunlong.qing.anime.domain.watchhistory.repository.WatchHistoryRepository;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository.WatchHistoryJpaRepository;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.WatchHistoryEntity;
import cn.chenyunlong.qing.anime.domain.watchhistory.WatchHistoryId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 观看历史仓储实现类
 * 使用JPA数据库实现
 *
 * @author 陈云龙
 */
@Repository
@RequiredArgsConstructor
public class WatchHistoryRepositoryImpl implements WatchHistoryRepository {

    private final WatchHistoryJpaRepository watchHistoryJpaRepository;

    @Override
    public WatchHistory save(WatchHistory history) {
        WatchHistoryEntity entity = toEntity(history);
        WatchHistoryEntity savedEntity = watchHistoryJpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public List<WatchHistory> findByUserId(Long userId) {
        return watchHistoryJpaRepository.findByUserIdOrderByWatchTimeDesc(userId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<WatchHistory> findByUserIdAndTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        return watchHistoryJpaRepository.findByUserIdAndWatchTimeBetweenOrderByWatchTimeDesc(userId, startTime, endTime)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<WatchHistory> findRecentByUserId(Long userId, int limit) {
        return watchHistoryJpaRepository.findByUserIdOrderByWatchTimeDesc(userId)
                .stream()
                .map(this::toDomain)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<WatchHistory> findByUserIdAndAnimeId(Long userId, Long animeId) {
        return watchHistoryJpaRepository.findByUserIdAndAnimeIdOrderByWatchTimeDesc(userId, animeId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Long countByAnimeId(Long animeId) {
        return watchHistoryJpaRepository.countByAnimeId(animeId);
    }

    @Override
    public Long sumWatchDurationByUserId(Long userId) {
        Long totalTime = watchHistoryJpaRepository.sumWatchDurationByUserId(userId);
        return totalTime != null ? totalTime : 0L;
    }

    @Override
    public void deleteById(Long id) {
        watchHistoryJpaRepository.deleteById(id);
    }

    @Override
    public void deleteByUserId(Long userId) {
        watchHistoryJpaRepository.deleteByUserId(userId);
    }

    private WatchHistoryEntity toEntity(WatchHistory watchHistory) {
        WatchHistoryEntity entity = new WatchHistoryEntity();
        if (watchHistory.getId() != null) {
            entity.setId(watchHistory.getId().getValue());
        }
        entity.setUserId(watchHistory.getUserId());
        entity.setAnimeId(watchHistory.getAnimeId());
        entity.setEpisodeId(watchHistory.getEpisodeId());
        entity.setWatchTime(watchHistory.getWatchTime());
        entity.setWatchDuration(watchHistory.getWatchDuration().longValue());
        entity.setDeviceType(watchHistory.getDeviceType());
        entity.setIpAddress(watchHistory.getIpAddress());
        entity.setUserAgent(watchHistory.getUserAgent());
        return entity;
    }

    private WatchHistory toDomain(WatchHistoryEntity entity) {
        WatchHistory watchHistory = WatchHistory.create(
            entity.getUserId(),
            entity.getAnimeId(),
            entity.getEpisodeId(),
            entity.getWatchDuration().intValue(),
            entity.getDeviceType(),
            entity.getIpAddress(),
            entity.getUserAgent()
        );
        watchHistory.setId(WatchHistoryId.of(entity.getId()));
        watchHistory.setWatchTime(entity.getWatchTime());
        return watchHistory;
    }
}
