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

import cn.chenyunlong.qing.anime.domain.rating.Rating;
import cn.chenyunlong.qing.anime.domain.rating.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 评分评论应用服务
 *
 * @author 陈云龙
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class RatingService {

    private final RatingRepository ratingRepository;

    /**
     * 添加评分评论
     */
    public Rating addRating(Long userId, Long animeId, Integer score, String comment, 
                          Boolean isAnonymous, Boolean isPublic) {
        // 检查用户是否已评分
        if (ratingRepository.existsByUserIdAndAnimeId(userId, animeId)) {
            throw new IllegalStateException("您已经评分过该动漫");
        }
        
        Rating rating = Rating.create(userId, animeId, score, comment, isAnonymous, isPublic);
        Rating saved = ratingRepository.save(rating);
        log.info("用户 {} 对动漫 {} 评分 {} 分", userId, animeId, score);
        return saved;
    }

    /**
     * 更新评分
     */
    public void updateScore(Long userId, Long animeId, Integer score) {
        Optional<Rating> ratingOpt = ratingRepository.findByUserIdAndAnimeId(userId, animeId);
        if (ratingOpt.isPresent()) {
            Rating rating = ratingOpt.get();
            rating.updateScore(score);
            ratingRepository.save(rating);
            log.info("用户 {} 更新动漫 {} 的评分为 {} 分", userId, animeId, score);
        } else {
            throw new IllegalStateException("评分记录不存在");
        }
    }

    /**
     * 更新评论
     */
    public void updateComment(Long userId, Long animeId, String comment) {
        Optional<Rating> ratingOpt = ratingRepository.findByUserIdAndAnimeId(userId, animeId);
        if (ratingOpt.isPresent()) {
            Rating rating = ratingOpt.get();
            rating.updateComment(comment);
            ratingRepository.save(rating);
            log.info("用户 {} 更新动漫 {} 的评论", userId, animeId);
        } else {
            throw new IllegalStateException("评分记录不存在");
        }
    }

    /**
     * 点赞评论
     */
    public void likeRating(Long userId, Long animeId) {
        Optional<Rating> ratingOpt = ratingRepository.findByUserIdAndAnimeId(userId, animeId);
        if (ratingOpt.isPresent()) {
            Rating rating = ratingOpt.get();
            rating.incrementLikeCount();
            ratingRepository.save(rating);
            log.info("评论获得点赞，用户 {} 动漫 {}", userId, animeId);
        } else {
            throw new IllegalStateException("评分记录不存在");
        }
    }

    /**
     * 取消点赞
     */
    public void unlikeRating(Long userId, Long animeId) {
        Optional<Rating> ratingOpt = ratingRepository.findByUserIdAndAnimeId(userId, animeId);
        if (ratingOpt.isPresent()) {
            Rating rating = ratingOpt.get();
            rating.decrementLikeCount();
            ratingRepository.save(rating);
            log.info("取消点赞评论，用户 {} 动漫 {}", userId, animeId);
        } else {
            throw new IllegalStateException("评分记录不存在");
        }
    }

    /**
     * 设置匿名状态
     */
    public void setAnonymousStatus(Long userId, Long animeId, Boolean isAnonymous) {
        Optional<Rating> ratingOpt = ratingRepository.findByUserIdAndAnimeId(userId, animeId);
        if (ratingOpt.isPresent()) {
            Rating rating = ratingOpt.get();
            rating.setAnonymousStatus(isAnonymous);
            ratingRepository.save(rating);
            log.info("用户 {} 设置动漫 {} 评论匿名状态为 {}", userId, animeId, isAnonymous);
        } else {
            throw new IllegalStateException("评分记录不存在");
        }
    }

    /**
     * 设置公开状态
     */
    public void setPublicStatus(Long userId, Long animeId, Boolean isPublic) {
        Optional<Rating> ratingOpt = ratingRepository.findByUserIdAndAnimeId(userId, animeId);
        if (ratingOpt.isPresent()) {
            Rating rating = ratingOpt.get();
            rating.setPublicStatus(isPublic);
            ratingRepository.save(rating);
            log.info("用户 {} 设置动漫 {} 评论公开状态为 {}", userId, animeId, isPublic);
        } else {
            throw new IllegalStateException("评分记录不存在");
        }
    }

    /**
     * 删除评分评论
     */
    public void deleteRating(Long userId, Long animeId) {
        ratingRepository.deleteByUserIdAndAnimeId(userId, animeId);
        log.info("用户 {} 删除动漫 {} 的评分评论", userId, animeId);
    }

    /**
     * 获取动漫的所有评分
     */
    public List<Rating> getAnimeRatings(Long animeId) {
        return ratingRepository.findPublicByAnimeId(animeId);
    }

    /**
     * 获取用户的评分记录
     */
    public List<Rating> getUserRatings(Long userId) {
        return ratingRepository.findByUserId(userId);
    }

    /**
     * 获取用户对某个动漫的评分
     */
    public Optional<Rating> getUserAnimeRating(Long userId, Long animeId) {
        return ratingRepository.findByUserIdAndAnimeId(userId, animeId);
    }

    /**
     * 计算动漫平均评分
     */
    public Double getAnimeAverageScore(Long animeId) {
        return ratingRepository.calculateAverageScoreByAnimeId(animeId);
    }

    /**
     * 获取动漫评分次数
     */
    public Long getAnimeRatingCount(Long animeId) {
        return ratingRepository.countByAnimeId(animeId);
    }

    /**
     * 获取热门评论
     */
    public List<Rating> getPopularComments(Long animeId, int limit) {
        return ratingRepository.findPopularCommentsByAnimeId(animeId, limit);
    }

    /**
     * 获取最新评论
     */
    public List<Rating> getLatestComments(Long animeId, int limit) {
        return ratingRepository.findLatestCommentsByAnimeId(animeId, limit);
    }

    /**
     * 检查用户是否已评分
     */
    public Boolean hasUserRated(Long userId, Long animeId) {
        return ratingRepository.existsByUserIdAndAnimeId(userId, animeId);
    }
}
