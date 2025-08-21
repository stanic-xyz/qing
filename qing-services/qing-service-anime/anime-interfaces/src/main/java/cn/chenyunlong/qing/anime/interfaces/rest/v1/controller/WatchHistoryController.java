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

import cn.chenyunlong.qing.anime.application.service.WatchHistoryService;
import cn.chenyunlong.qing.anime.domain.watchhistory.WatchHistory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 观看历史控制器
 *
 * @author 陈云龙
 */
@RestController
@RequestMapping("/api/v1/watch-history")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "观看历史管理", description = "观看历史相关接口")
public class WatchHistoryController {

    private final WatchHistoryService watchHistoryService;

    @PostMapping("/record")
    @Operation(summary = "记录观看历史", description = "记录用户的观看历史")
    public ResponseEntity<WatchHistory> recordWatchHistory(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "动漫ID") @RequestParam Long animeId,
            @Parameter(description = "剧集ID") @RequestParam Long episodeId,
            @Parameter(description = "设备类型") @RequestParam(required = false) String deviceType,
            HttpServletRequest request) {
        
        String ipAddress = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        
        WatchHistory history = watchHistoryService.recordWatchHistory(
                userId, animeId, episodeId, deviceType, ipAddress, userAgent);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/update-duration")
    @Operation(summary = "更新观看时长", description = "更新观看历史的观看时长")
    public ResponseEntity<Void> updateWatchDuration(
            @Parameter(description = "历史记录ID") @RequestParam Long historyId,
            @Parameter(description = "观看时长(秒)") @RequestParam Long watchDuration) {
        
        watchHistoryService.updateWatchDuration(historyId, watchDuration);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户观看历史", description = "获取用户的所有观看历史")
    public ResponseEntity<List<WatchHistory>> getUserWatchHistory(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        
        List<WatchHistory> historyList = watchHistoryService.getUserWatchHistory(userId);
        return ResponseEntity.ok(historyList);
    }

    @GetMapping("/user/{userId}/recent")
    @Operation(summary = "获取最近观看历史", description = "获取用户最近的观看历史")
    public ResponseEntity<List<WatchHistory>> getRecentWatchHistory(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "20") int limit) {
        
        List<WatchHistory> recentList = watchHistoryService.getRecentWatchHistory(userId, limit);
        return ResponseEntity.ok(recentList);
    }

    @GetMapping("/user/{userId}/anime/{animeId}")
    @Operation(summary = "获取动漫观看历史", description = "获取用户在某个动漫的观看历史")
    public ResponseEntity<List<WatchHistory>> getAnimeWatchHistory(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "动漫ID") @PathVariable Long animeId) {
        
        List<WatchHistory> historyList = watchHistoryService.getAnimeWatchHistory(userId, animeId);
        return ResponseEntity.ok(historyList);
    }

    @GetMapping("/user/{userId}/time-range")
    @Operation(summary = "按时间范围获取观看历史", description = "获取用户在指定时间范围内的观看历史")
    public ResponseEntity<List<WatchHistory>> getWatchHistoryByTimeRange(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "开始时间") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        
        List<WatchHistory> historyList = watchHistoryService.getWatchHistoryByTimeRange(userId, startTime, endTime);
        return ResponseEntity.ok(historyList);
    }

    @GetMapping("/anime/{animeId}/watch-count")
    @Operation(summary = "获取动漫观看次数", description = "统计动漫的总观看次数")
    public ResponseEntity<Long> getAnimeWatchCount(
            @Parameter(description = "动漫ID") @PathVariable Long animeId) {
        
        Long watchCount = watchHistoryService.getAnimeWatchCount(animeId);
        return ResponseEntity.ok(watchCount);
    }

    @GetMapping("/user/{userId}/total-duration")
    @Operation(summary = "获取用户总观看时长", description = "统计用户的总观看时长")
    public ResponseEntity<Long> getUserTotalWatchDuration(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        
        Long totalDuration = watchHistoryService.getUserTotalWatchDuration(userId);
        return ResponseEntity.ok(totalDuration);
    }

    @DeleteMapping("/{historyId}")
    @Operation(summary = "删除观看历史", description = "删除指定的观看历史记录")
    public ResponseEntity<Void> deleteWatchHistory(
            @Parameter(description = "历史记录ID") @PathVariable Long historyId) {
        
        watchHistoryService.deleteWatchHistory(historyId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/user/{userId}/clear")
    @Operation(summary = "清空用户观看历史", description = "清空用户的所有观看历史")
    public ResponseEntity<Void> clearUserWatchHistory(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        
        watchHistoryService.clearUserWatchHistory(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取客户端真实IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}