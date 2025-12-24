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

import cn.chenyunlong.qing.anime.domain.playback.PlaybackProgress;
import cn.chenyunlong.qing.anime.domain.playback.PlaybackProgressId;
import cn.chenyunlong.qing.anime.domain.playback.repository.PlaybackProgressRepository;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.PlaybackProgressEntity;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository.PlaybackProgressJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 播放进度仓储实现类
 * 使用JPA数据库实现
 *
 * @author 陈云龙
 */
@Repository
@RequiredArgsConstructor
public class PlaybackProgressRepositoryImpl implements PlaybackProgressRepository {

    private final PlaybackProgressJpaRepository playbackProgressJpaRepository;

    @Override
    public PlaybackProgress save(PlaybackProgress progress) {
        PlaybackProgressEntity entity = toEntity(progress);
        PlaybackProgressEntity savedEntity = playbackProgressJpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<PlaybackProgress> findByUserIdAndEpisodeId(Long userId, Long episodeId) {
        return playbackProgressJpaRepository.findByUserIdAndEpisodeId(userId, episodeId)
                .map(this::toDomain);
    }

    @Override
    public List<PlaybackProgress> findByUserIdAndAnimeId(Long userId, Long animeId) {
        return playbackProgressJpaRepository.findByUserIdAndAnimeIdOrderByLastWatchTimeDesc(userId, animeId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaybackProgress> findByUserId(Long userId) {
        return playbackProgressJpaRepository.findByUserIdOrderByLastWatchTimeDesc(userId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaybackProgress> findRecentByUserId(Long userId, int limit) {
        return playbackProgressJpaRepository.findByUserIdOrderByLastWatchTimeDesc(userId)
                .stream()
                .limit(limit)
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaybackProgress> findCompletedByUserId(Long userId) {
        return playbackProgressJpaRepository.findByUserIdAndIsCompletedTrueOrderByLastWatchTimeDesc(userId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        playbackProgressJpaRepository.deleteById(id);
    }

    @Override
    public void deleteByUserIdAndAnimeId(Long userId, Long animeId) {
        playbackProgressJpaRepository.deleteByUserIdAndAnimeId(userId, animeId);
    }

    private PlaybackProgressEntity toEntity(PlaybackProgress progress) {
        PlaybackProgressEntity entity = new PlaybackProgressEntity();
        if (progress.getId() != null) {
            entity.setId(progress.getId().id());
        }
        entity.setUserId(progress.getUserId());
        entity.setAnimeId(progress.getAnimeId());
        entity.setEpisodeId(progress.getEpisodeId());
        entity.setCurrentPosition(progress.getProgressSeconds().longValue());
        entity.setTotalDuration(progress.getTotalSeconds().longValue());
        entity.setProgressPercentage(BigDecimal.valueOf(progress.getProgressPercentage()));
        entity.setIsCompleted(progress.getIsCompleted());
        entity.setLastWatchTime(progress.getLastPlayTime());
        entity.setPlayCount(progress.getPlayCount());
        return entity;
    }

    private PlaybackProgress toDomain(PlaybackProgressEntity entity) {
        // 使用反射创建PlaybackProgress实例
        PlaybackProgress progress = PlaybackProgress.create(
                entity.getUserId(),
                entity.getAnimeId(),
                entity.getEpisodeId(),
            entity.getTotalDuration().intValue()
        );
        progress.setId(PlaybackProgressId.of(entity.getId()));
        progress.updateProgress(entity.getCurrentPosition().intValue());
        progress.setIsCompleted(entity.getIsCompleted());
        progress.setLastPlayTime(entity.getLastWatchTime());
        progress.setPlayCount(entity.getPlayCount());
        return progress;
    }
}
