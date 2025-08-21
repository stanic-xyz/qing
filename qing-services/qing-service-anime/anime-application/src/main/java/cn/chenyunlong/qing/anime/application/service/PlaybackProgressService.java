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

import cn.chenyunlong.qing.anime.domain.playback.PlaybackProgress;
import cn.chenyunlong.qing.anime.domain.playback.repository.PlaybackProgressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import java.util.List;
import java.util.Optional;

/**
 * 播放进度应用服务
 *
 * @author 陈云龙
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class PlaybackProgressService {

    private final PlaybackProgressRepository playbackProgressRepository;

    /**
     * 更新播放进度
     */
    public PlaybackProgress updateProgress(Long userId, Long animeId, Long episodeId, 
                                         Long currentPosition, Long totalDuration) {
        Optional<PlaybackProgress> existingProgress = 
            playbackProgressRepository.findByUserIdAndEpisodeId(userId, episodeId);
        
        PlaybackProgress progress;
        if (existingProgress.isPresent()) {
            progress = existingProgress.get();
            progress.updateProgress(currentPosition.intValue());
        } else {
            progress = PlaybackProgress.create(userId, animeId, episodeId, totalDuration.intValue());
            progress.updateProgress(currentPosition.intValue());
        }
        
        return playbackProgressRepository.save(progress);
    }

    /**
     * 标记剧集为已完成
     */
    public void markAsCompleted(Long userId, Long animeId, Long episodeId, Long totalDuration) {
        Optional<PlaybackProgress> existingProgress = 
            playbackProgressRepository.findByUserIdAndEpisodeId(userId, episodeId);
        
        PlaybackProgress progress;
        if (existingProgress.isPresent()) {
            progress = existingProgress.get();
            progress.markAsCompleted();
        } else {
            progress = PlaybackProgress.create(userId, animeId, episodeId, totalDuration.intValue());
            progress.markAsCompleted();
        }
        
        playbackProgressRepository.save(progress);
        log.info("用户 {} 完成观看剧集 {}", userId, episodeId);
    }

    /**
     * 增加播放次数
     */
    public void incrementPlayCount(Long userId, Long episodeId) {
        Optional<PlaybackProgress> progress = 
            playbackProgressRepository.findByUserIdAndEpisodeId(userId, episodeId);
        
        if (progress.isPresent()) {
            progress.get().incrementPlayCount();
            playbackProgressRepository.save(progress.get());
        }
    }

    /**
     * 获取用户的播放进度
     */
    public Optional<PlaybackProgress> getProgress(Long userId, Long episodeId) {
        return playbackProgressRepository.findByUserIdAndEpisodeId(userId, episodeId);
    }

    /**
     * 获取用户在某个动漫的所有播放进度
     */
    public List<PlaybackProgress> getAnimeProgress(Long userId, Long animeId) {
        return playbackProgressRepository.findByUserIdAndAnimeId(userId, animeId);
    }

    /**
     * 获取用户最近观看的动漫
     */
    public List<PlaybackProgress> getRecentWatched(Long userId, int limit) {
        return playbackProgressRepository.findRecentByUserId(userId, limit);
    }

    /**
     * 获取用户已完成观看的剧集
     */
    public List<PlaybackProgress> getCompletedEpisodes(Long userId) {
        return playbackProgressRepository.findCompletedByUserId(userId);
    }

    /**
     * 重置播放进度
     */
    public void resetProgress(Long userId, Long episodeId) {
        Optional<PlaybackProgress> progress = 
            playbackProgressRepository.findByUserIdAndEpisodeId(userId, episodeId);
        
        if (progress.isPresent()) {
            progress.get().reset();
            playbackProgressRepository.save(progress.get());
            log.info("重置用户 {} 剧集 {} 的播放进度", userId, episodeId);
        }
    }

    /**
     * 删除播放进度
     */
    public void deleteProgress(Long userId, Long episodeId) {
        // 需要先查找到具体的播放进度记录，然后删除
        Optional<PlaybackProgress> progress = playbackProgressRepository.findByUserIdAndEpisodeId(userId, episodeId);
        if (progress.isPresent()) {
            playbackProgressRepository.deleteById(progress.get().getId().getId());
            log.info("删除用户 {} 剧集 {} 的播放进度", userId, episodeId);
        }
    }

    /**
     * 删除用户在某个动漫的所有播放进度
     */
    public void deleteAnimeProgress(Long userId, Long animeId) {
        playbackProgressRepository.deleteByUserIdAndAnimeId(userId, animeId);
        log.info("删除用户 {} 动漫 {} 的所有播放进度", userId, animeId);
    }
}