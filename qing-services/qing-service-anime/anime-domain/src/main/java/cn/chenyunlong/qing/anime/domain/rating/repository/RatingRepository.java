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

package cn.chenyunlong.qing.anime.domain.rating.repository;

import cn.chenyunlong.qing.anime.domain.rating.Rating;

import java.util.List;
import java.util.Optional;

/**
 * 评分评论仓储接口
 *
 * @author 陈云龙
 */
public interface RatingRepository {

    /**
     * 保存评分记录
     */
    Rating save(Rating rating);

    /**
     * 根据用户ID和动漫ID查找评分记录
     */
    Optional<Rating> findByUserIdAndAnimeId(Long userId, Long animeId);

    /**
     * 根据动漫ID查找所有评分记录
     */
    List<Rating> findByAnimeId(Long animeId);

    /**
     * 根据动漫ID查找公开的评分记录
     */
    List<Rating> findPublicByAnimeId(Long animeId);

    /**
     * 根据用户ID查找评分记录
     */
    List<Rating> findByUserId(Long userId);

    /**
     * 根据动漫ID计算平均评分
     */
    Double calculateAverageScoreByAnimeId(Long animeId);

    /**
     * 根据动漫ID统计评分次数
     */
    Long countByAnimeId(Long animeId);

    /**
     * 根据动漫ID和评分范围统计评分次数
     */
    Long countByAnimeIdAndScoreRange(Long animeId, Integer minScore, Integer maxScore);

    /**
     * 查找热门评论（按点赞数排序）
     */
    List<Rating> findPopularCommentsByAnimeId(Long animeId, int limit);

    /**
     * 查找最新评论
     */
    List<Rating> findLatestCommentsByAnimeId(Long animeId, int limit);

    /**
     * 检查用户是否已评分
     */
    Boolean existsByUserIdAndAnimeId(Long userId, Long animeId);

    /**
     * 删除评分记录
     */
    void deleteById(Long id);

    /**
     * 根据用户ID和动漫ID删除评分记录
     */
    void deleteByUserIdAndAnimeId(Long userId, Long animeId);
}