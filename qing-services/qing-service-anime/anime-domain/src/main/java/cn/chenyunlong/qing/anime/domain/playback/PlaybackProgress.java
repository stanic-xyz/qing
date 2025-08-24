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

package cn.chenyunlong.qing.anime.domain.playback;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 播放进度聚合根
 * 记录用户观看动漫的进度信息
 *
 * @author 陈云龙
 */
@Getter
@Setter
public class PlaybackProgress extends BaseAggregate<PlaybackProgressId> {

    @FieldDesc(description = "用户ID")
    private Long userId;

    @FieldDesc(description = "动漫ID")
    private Long animeId;

    @FieldDesc(description = "剧集ID")
    private Long episodeId;

    @FieldDesc(description = "播放进度(秒)")
    private Integer progressSeconds;

    @FieldDesc(description = "总时长(秒)")
    private Integer totalSeconds;

    @FieldDesc(description = "播放进度百分比")
    private Double progressPercentage;

    @FieldDesc(description = "是否已完成观看")
    private Boolean isCompleted;

    @FieldDesc(description = "最后播放时间")
    private LocalDateTime lastPlayTime;

    @FieldDesc(description = "播放次数")
    private Integer playCount;

    /**
     * 受保护的构造函数
     */
    protected PlaybackProgress() {}

    /**
     * 创建播放进度记录
     */
    public static PlaybackProgress create(Long userId, Long animeId, Long episodeId, Integer totalSeconds) {
        PlaybackProgress progress = new PlaybackProgress();
        progress.userId = userId;
        progress.animeId = animeId;
        progress.episodeId = episodeId;
        progress.totalSeconds = totalSeconds;
        progress.progressSeconds = 0;
        progress.progressPercentage = 0.0;
        progress.isCompleted = false;
        progress.lastPlayTime = LocalDateTime.now();
        progress.playCount = 1;
        return progress;
    }

    /**
     * 更新播放进度
     */
    public void updateProgress(Integer progressSeconds) {
        if (progressSeconds < 0 || progressSeconds > this.totalSeconds) {
            throw new IllegalArgumentException("播放进度不能小于0或大于总时长");
        }
        
        this.progressSeconds = progressSeconds;
        this.progressPercentage = (double) progressSeconds / totalSeconds * 100;
        this.lastPlayTime = LocalDateTime.now();
        
        // 如果播放进度超过95%，认为已完成观看
        if (this.progressPercentage >= 95.0) {
            this.isCompleted = true;
        }
    }

    /**
     * 增加播放次数
     */
    public void incrementPlayCount() {
        this.playCount++;
        this.lastPlayTime = LocalDateTime.now();
    }

    /**
     * 标记为已完成
     */
    public void markAsCompleted() {
        this.isCompleted = true;
        this.progressSeconds = this.totalSeconds;
        this.progressPercentage = 100.0;
        this.lastPlayTime = LocalDateTime.now();
    }

    /**
     * 重置播放进度
     */
    public void reset() {
        this.progressSeconds = 0;
        this.progressPercentage = 0.0;
        this.isCompleted = false;
        this.lastPlayTime = LocalDateTime.now();
    }
}
