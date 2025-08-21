package cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository;

import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingJpaRepository extends JpaRepository<RatingEntity, Long> {

    /**
     * 根据用户ID和动漫ID查找评分记录
     */
    Optional<RatingEntity> findByUserIdAndAnimeId(Long userId, Long animeId);

    /**
     * 根据动漫ID查找所有评分记录
     */
    List<RatingEntity> findByAnimeIdOrderByRatingTimeDesc(Long animeId);

    /**
     * 根据用户ID查找所有评分记录
     */
    List<RatingEntity> findByUserIdOrderByRatingTimeDesc(Long userId);

    /**
     * 计算动漫的平均评分
     */
    @Query("SELECT AVG(r.score) FROM RatingEntity r WHERE r.animeId = :animeId")
    Double findAverageScoreByAnimeId(@Param("animeId") Long animeId);

    /**
     * 统计动漫的评分数量
     */
    long countByAnimeId(Long animeId);

    /**
     * 根据评分范围查找动漫评分
     */
    List<RatingEntity> findByAnimeIdAndScoreBetweenOrderByRatingTimeDesc(Long animeId, Integer minScore, Integer maxScore);

    /**
     * 检查用户是否已评分
     */
    boolean existsByUserIdAndAnimeId(Long userId, Long animeId);

    /**
     * 根据动漫ID查找公开的评分记录
     */
    List<RatingEntity> findByAnimeIdAndIsPublicTrueOrderByRatingTimeDesc(Long animeId);

    /**
     * 统计评分范围内的数量
     */
    long countByAnimeIdAndScoreBetween(Long animeId, Integer minScore, Integer maxScore);

    /**
     * 根据动漫ID查找有评论且按点赞数排序的记录
     */
    List<RatingEntity> findByAnimeIdAndCommentIsNotNullOrderByLikeCountDesc(Long animeId);

    /**
     * 根据动漫ID查找有评论且按评分时间排序的记录
     */
    List<RatingEntity> findByAnimeIdAndCommentIsNotNullOrderByRatingTimeDesc(Long animeId);

    /**
     * 根据用户ID和动漫ID删除评分记录
     */
    void deleteByUserIdAndAnimeId(Long userId, Long animeId);
}
