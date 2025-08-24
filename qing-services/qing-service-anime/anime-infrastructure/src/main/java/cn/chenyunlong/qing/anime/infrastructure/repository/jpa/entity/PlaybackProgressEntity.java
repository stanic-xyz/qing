package cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.jpa.support.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "anime_playback_progress")
public class PlaybackProgressEntity extends BaseJpaEntity {

    @FieldDesc(description = "用户ID")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @FieldDesc(description = "动漫ID")
    @Column(name = "anime_id", nullable = false)
    private Long animeId;

    @FieldDesc(description = "剧集ID")
    @Column(name = "episode_id", nullable = false)
    private Long episodeId;

    @FieldDesc(description = "当前播放位置(秒)")
    @Column(name = "current_position", nullable = false)
    private Long currentPosition = 0L;

    @FieldDesc(description = "总时长(秒)")
    @Column(name = "total_duration", nullable = false)
    private Long totalDuration = 0L;

    @FieldDesc(description = "播放进度百分比")
    @Column(name = "progress_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal progressPercentage = BigDecimal.ZERO;

    @FieldDesc(description = "是否已完成观看")
    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;

    @FieldDesc(description = "最后播放时间")
    @Column(name = "last_watch_time", nullable = false)
    private LocalDateTime lastWatchTime;

    @FieldDesc(description = "播放次数")
    @Column(name = "play_count", nullable = false)
    private Integer playCount = 0;
}
