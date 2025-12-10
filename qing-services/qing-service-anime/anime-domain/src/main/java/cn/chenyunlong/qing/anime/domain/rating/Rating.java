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

package cn.chenyunlong.qing.anime.domain.rating;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.qing.domain.common.BaseSimpleBusinessEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 评分评论聚合根
 * 管理用户对动漫的评分和评论
 *
 * @author 陈云龙
 */
@Getter
@Setter
public class Rating extends BaseSimpleBusinessEntity<RatingId> {

    @FieldDesc(description = "用户ID")
    private Long userId;

    @FieldDesc(description = "动漫ID")
    private Long animeId;

    @FieldDesc(description = "评分(1-10)")
    private Integer score;

    @FieldDesc(description = "评论内容")
    private String comment;

    @FieldDesc(description = "评分时间")
    private LocalDateTime ratingTime;

    @FieldDesc(description = "最后更新时间")
    private LocalDateTime lastUpdateTime;

    @FieldDesc(description = "是否匿名")
    private Boolean isAnonymous;

    @FieldDesc(description = "点赞数")
    private Integer likeCount;

    @FieldDesc(description = "是否公开")
    private Boolean isPublic;

    /**
     * 受保护的构造函数
     */
    protected Rating() {
    }

    /**
     * 创建评分记录
     */
    public static Rating create(Long userId, Long animeId, Integer score, String comment,
            Boolean isAnonymous, Boolean isPublic) {
        if (score < 1 || score > 10) {
            throw new IllegalArgumentException("评分必须在1-10之间");
        }

        Rating rating = new Rating();
        rating.userId = userId;
        rating.animeId = animeId;
        rating.score = score;
        rating.comment = comment;
        rating.ratingTime = LocalDateTime.now();
        rating.lastUpdateTime = LocalDateTime.now();
        rating.isAnonymous = isAnonymous != null ? isAnonymous : false;
        rating.isPublic = isPublic != null ? isPublic : true;
        rating.likeCount = 0;
        return rating;
    }

    /**
     * 更新评分
     */
    public void updateScore(Integer score) {
        if (score < 1 || score > 10) {
            throw new IllegalArgumentException("评分必须在1-10之间");
        }
        this.score = score;
        this.lastUpdateTime = LocalDateTime.now();
    }

    /**
     * 更新评论
     */
    public void updateComment(String comment) {
        this.comment = comment;
        this.lastUpdateTime = LocalDateTime.now();
    }

    /**
     * 增加点赞数
     */
    public void incrementLikeCount() {
        this.likeCount++;
    }

    /**
     * 减少点赞数
     */
    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    /**
     * 设置匿名状态
     */
    public void setAnonymousStatus(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
        this.lastUpdateTime = LocalDateTime.now();
    }

    /**
     * 设置公开状态
     */
    public void setPublicStatus(Boolean isPublic) {
        this.isPublic = isPublic;
        this.lastUpdateTime = LocalDateTime.now();
    }
}
