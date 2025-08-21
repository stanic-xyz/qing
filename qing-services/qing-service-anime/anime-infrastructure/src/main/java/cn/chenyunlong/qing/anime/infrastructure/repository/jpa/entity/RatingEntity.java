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
@Table(name = "anime_rating", 
       uniqueConstraints = @UniqueConstraint(name = "uk_user_anime", columnNames = {"user_id", "anime_id"}))
public class RatingEntity extends BaseJpaEntity {

    @FieldDesc(description = "用户ID")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @FieldDesc(description = "动漫ID")
    @Column(name = "anime_id", nullable = false)
    private Long animeId;

    @FieldDesc(description = "评分(1-10)")
    @Column(name = "score", nullable = false)
    private Integer score;

    @FieldDesc(description = "评论内容")
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @FieldDesc(description = "评分时间")
    @Column(name = "rating_time", nullable = false)
    private LocalDateTime ratingTime;

    @FieldDesc(description = "最后更新时间")
    @Column(name = "last_update_time", nullable = false)
    private LocalDateTime lastUpdateTime;

    @FieldDesc(description = "是否匿名")
    @Column(name = "is_anonymous", nullable = false)
    private Boolean isAnonymous = false;

    @FieldDesc(description = "点赞数")
    @Column(name = "like_count", nullable = false)
    private Integer likeCount = 0;

    @FieldDesc(description = "是否公开")
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = true;
}