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

package cn.chenyunlong.qing.anime.interfaces.rest.v1.controller;

import cn.chenyunlong.qing.anime.application.service.PlaybackProgressService;
import cn.chenyunlong.qing.anime.domain.playback.PlaybackProgress;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 播放进度控制器
 *
 * @author 陈云龙
 */
@RestController
@RequestMapping("/api/v1/playback-progress")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "播放进度管理", description = "播放进度相关接口")
public class PlaybackProgressController {

    private final PlaybackProgressService playbackProgressService;

    @PostMapping("/update")
    @Operation(summary = "更新播放进度", description = "更新用户的播放进度")
    public ResponseEntity<PlaybackProgress> updateProgress(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "动漫ID") @RequestParam Long animeId,
            @Parameter(description = "剧集ID") @RequestParam Long episodeId,
            @Parameter(description = "当前播放位置(秒)") @RequestParam Long currentPosition,
            @Parameter(description = "总时长(秒)") @RequestParam Long totalDuration) {
        
        PlaybackProgress progress = playbackProgressService.updateProgress(
                userId, animeId, episodeId, currentPosition, totalDuration);
        return ResponseEntity.ok(progress);
    }

    @PostMapping("/complete")
    @Operation(summary = "标记为已完成", description = "标记剧集为已完成观看")
    public ResponseEntity<Void> markAsCompleted(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "动漫ID") @RequestParam Long animeId,
            @Parameter(description = "剧集ID") @RequestParam Long episodeId,
            @Parameter(description = "总时长(秒)") @RequestParam Long totalDuration) {
        
        playbackProgressService.markAsCompleted(userId, animeId, episodeId, totalDuration);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/increment-play-count")
    @Operation(summary = "增加播放次数", description = "增加剧集的播放次数")
    public ResponseEntity<Void> incrementPlayCount(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "剧集ID") @RequestParam Long episodeId) {
        
        playbackProgressService.incrementPlayCount(userId, episodeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/episode/{episodeId}")
    @Operation(summary = "获取播放进度", description = "获取用户在某个剧集的播放进度")
    public ResponseEntity<PlaybackProgress> getProgress(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "剧集ID") @PathVariable Long episodeId) {
        
        Optional<PlaybackProgress> progress = playbackProgressService.getProgress(userId, episodeId);
        return progress.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/anime/{animeId}")
    @Operation(summary = "获取动漫播放进度", description = "获取用户在某个动漫的所有播放进度")
    public ResponseEntity<List<PlaybackProgress>> getAnimeProgress(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "动漫ID") @PathVariable Long animeId) {
        
        List<PlaybackProgress> progressList = playbackProgressService.getAnimeProgress(userId, animeId);
        return ResponseEntity.ok(progressList);
    }

    @GetMapping("/recent")
    @Operation(summary = "获取最近观看", description = "获取用户最近观看的动漫")
    public ResponseEntity<List<PlaybackProgress>> getRecentWatched(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "10") int limit) {
        
        List<PlaybackProgress> recentList = playbackProgressService.getRecentWatched(userId, limit);
        return ResponseEntity.ok(recentList);
    }

    @GetMapping("/completed")
    @Operation(summary = "获取已完成剧集", description = "获取用户已完成观看的剧集")
    public ResponseEntity<List<PlaybackProgress>> getCompletedEpisodes(
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        
        List<PlaybackProgress> completedList = playbackProgressService.getCompletedEpisodes(userId);
        return ResponseEntity.ok(completedList);
    }

    @PostMapping("/reset")
    @Operation(summary = "重置播放进度", description = "重置用户在某个剧集的播放进度")
    public ResponseEntity<Void> resetProgress(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "剧集ID") @RequestParam Long episodeId) {
        
        playbackProgressService.resetProgress(userId, episodeId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/episode")
    @Operation(summary = "删除播放进度", description = "删除用户在某个剧集的播放进度")
    public ResponseEntity<Void> deleteProgress(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "剧集ID") @RequestParam Long episodeId) {
        
        playbackProgressService.deleteProgress(userId, episodeId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/anime")
    @Operation(summary = "删除动漫播放进度", description = "删除用户在某个动漫的所有播放进度")
    public ResponseEntity<Void> deleteAnimeProgress(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "动漫ID") @RequestParam Long animeId) {
        
        playbackProgressService.deleteAnimeProgress(userId, animeId);
        return ResponseEntity.ok().build();
    }
}