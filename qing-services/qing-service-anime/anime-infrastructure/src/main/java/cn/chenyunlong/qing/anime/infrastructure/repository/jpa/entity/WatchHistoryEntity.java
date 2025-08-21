package cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.jpa.support.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "anime_watch_history")
public class WatchHistoryEntity extends BaseJpaEntity {

    @FieldDesc(description = "用户ID")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @FieldDesc(description = "动漫ID")
    @Column(name = "anime_id", nullable = false)
    private Long animeId;

    @FieldDesc(description = "剧集ID")
    @Column(name = "episode_id", nullable = false)
    private Long episodeId;

    @FieldDesc(description = "观看时间")
    @Column(name = "watch_time", nullable = false)
    private LocalDateTime watchTime;

    @FieldDesc(description = "观看时长(秒)")
    @Column(name = "watch_duration", nullable = false)
    private Long watchDuration = 0L;

    @FieldDesc(description = "设备类型")
    @Column(name = "device_type", length = 50)
    private String deviceType;

    @FieldDesc(description = "IP地址")
    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @FieldDesc(description = "用户代理")
    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;
}