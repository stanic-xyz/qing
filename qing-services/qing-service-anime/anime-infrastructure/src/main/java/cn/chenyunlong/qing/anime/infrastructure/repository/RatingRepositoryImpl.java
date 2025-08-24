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

package cn.chenyunlong.qing.anime.infrastructure.repository;

import cn.chenyunlong.qing.anime.domain.rating.Rating;
import cn.chenyunlong.qing.anime.domain.rating.repository.RatingRepository;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository.RatingJpaRepository;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.RatingEntity;
import cn.chenyunlong.qing.anime.domain.rating.RatingId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 评分评论仓储实现类
 * 使用JPA数据库实现
 *
 * @author 陈云龙
 */
@Repository
@RequiredArgsConstructor
public class RatingRepositoryImpl implements RatingRepository {

    private final RatingJpaRepository ratingJpaRepository;

    @Override
    public Rating save(Rating rating) {
        RatingEntity entity = toEntity(rating);
        RatingEntity savedEntity = ratingJpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Rating> findByUserIdAndAnimeId(Long userId, Long animeId) {
        return ratingJpaRepository.findByUserIdAndAnimeId(userId, animeId)
                .map(this::toDomain);
    }

    @Override
    public List<Rating> findByAnimeId(Long animeId) {
        return ratingJpaRepository.findByAnimeIdOrderByRatingTimeDesc(animeId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Rating> findPublicByAnimeId(Long animeId) {
        return ratingJpaRepository.findByAnimeIdAndIsPublicTrueOrderByRatingTimeDesc(animeId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Rating> findByUserId(Long userId) {
        return ratingJpaRepository.findByUserIdOrderByRatingTimeDesc(userId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Double calculateAverageScoreByAnimeId(Long animeId) {
        Double average = ratingJpaRepository.findAverageScoreByAnimeId(animeId);
        return average != null ? average : 0.0;
    }

    @Override
    public Long countByAnimeId(Long animeId) {
        return ratingJpaRepository.countByAnimeId(animeId);
    }

    @Override
    public Long countByAnimeIdAndScoreRange(Long animeId, Integer minScore, Integer maxScore) {
        return ratingJpaRepository.countByAnimeIdAndScoreBetween(animeId, minScore, maxScore);
    }

    @Override
    public List<Rating> findPopularCommentsByAnimeId(Long animeId, int limit) {
        return ratingJpaRepository.findByAnimeIdAndCommentIsNotNullOrderByLikeCountDesc(animeId)
                .stream()
                .filter(entity -> entity.getComment() != null && !entity.getComment().trim().isEmpty())
                .limit(limit)
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Rating> findLatestCommentsByAnimeId(Long animeId, int limit) {
        return ratingJpaRepository.findByAnimeIdAndCommentIsNotNullOrderByRatingTimeDesc(animeId)
                .stream()
                .filter(entity -> entity.getComment() != null && !entity.getComment().trim().isEmpty())
                .limit(limit)
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean existsByUserIdAndAnimeId(Long userId, Long animeId) {
        return ratingJpaRepository.existsByUserIdAndAnimeId(userId, animeId);
    }

    @Override
    public void deleteById(Long id) {
        ratingJpaRepository.deleteById(id);
    }

    @Override
    public void deleteByUserIdAndAnimeId(Long userId, Long animeId) {
        ratingJpaRepository.deleteByUserIdAndAnimeId(userId, animeId);
    }

    private RatingEntity toEntity(Rating rating) {
        RatingEntity entity = new RatingEntity();
        if (rating.getId() != null) {
            entity.setId(rating.getId().getValue());
        }
        entity.setUserId(rating.getUserId());
        entity.setAnimeId(rating.getAnimeId());
        entity.setScore(rating.getScore());
        entity.setComment(rating.getComment());
        entity.setRatingTime(rating.getRatingTime());
        entity.setLastUpdateTime(rating.getLastUpdateTime());
        entity.setIsAnonymous(rating.getIsAnonymous());
        entity.setLikeCount(rating.getLikeCount());
        entity.setIsPublic(rating.getIsPublic());
        return entity;
    }

    private Rating toDomain(RatingEntity entity) {
        Rating rating = Rating.create(
            entity.getUserId(),
            entity.getAnimeId(),
            entity.getScore(),
            entity.getComment(),
            entity.getIsAnonymous(),
            entity.getIsPublic()
        );
        rating.setId(RatingId.of(entity.getId()));
        rating.setRatingTime(entity.getRatingTime());
        rating.setLastUpdateTime(entity.getLastUpdateTime());
        rating.setLikeCount(entity.getLikeCount());
        return rating;
    }
}
