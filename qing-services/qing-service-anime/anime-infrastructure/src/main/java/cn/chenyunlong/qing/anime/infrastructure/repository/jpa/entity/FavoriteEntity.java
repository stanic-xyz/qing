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
@Table(name = "anime_favorite", 
       uniqueConstraints = @UniqueConstraint(name = "uk_user_anime", columnNames = {"user_id", "anime_id"}))
public class FavoriteEntity extends BaseJpaEntity {

    @FieldDesc(description = "用户ID")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @FieldDesc(description = "动漫ID")
    @Column(name = "anime_id", nullable = false)
    private Long animeId;

    @FieldDesc(description = "收藏时间")
    @Column(name = "favorite_time", nullable = false)
    private LocalDateTime favoriteTime;

    @FieldDesc(description = "收藏分组")
    @Column(name = "favorite_group", nullable = false, length = 100)
    private String favoriteGroup = "默认分组";

    @FieldDesc(description = "备注")
    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

    @FieldDesc(description = "是否公开")
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = false;
}
