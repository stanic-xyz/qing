package cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository;

import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.PlaybackProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaybackProgressJpaRepository extends JpaRepository<PlaybackProgressEntity, Long> {

    /**
     * 根据用户ID和剧集ID查找播放进度
     */
    Optional<PlaybackProgressEntity> findByUserIdAndEpisodeId(Long userId, Long episodeId);

    /**
     * 根据用户ID和动漫ID查找所有播放进度
     */
    List<PlaybackProgressEntity> findByUserIdAndAnimeIdOrderByLastWatchTimeDesc(Long userId, Long animeId);

    /**
     * 根据用户ID查找所有播放进度，按最后观看时间倒序
     */
    List<PlaybackProgressEntity> findByUserIdOrderByLastWatchTimeDesc(Long userId);

    /**
     * 根据用户ID查找已完成的播放进度
     */
    List<PlaybackProgressEntity> findByUserIdAndIsCompletedTrueOrderByLastWatchTimeDesc(Long userId);

    /**
     * 根据用户ID和动漫ID删除播放进度
     */
    void deleteByUserIdAndAnimeId(Long userId, Long animeId);

    /**
     * 根据用户ID查找未完成的播放进度
     */
    List<PlaybackProgressEntity> findByUserIdAndIsCompletedFalseOrderByLastWatchTimeDesc(Long userId);

    /**
     * 根据用户ID和动漫ID查找未完成的播放进度
     */
    List<PlaybackProgressEntity> findByUserIdAndAnimeIdAndIsCompletedFalseOrderByLastWatchTimeDesc(Long userId, Long animeId);

    /**
     * 统计用户观看完成的剧集数量
     */
    @Query("SELECT COUNT(p) FROM PlaybackProgressEntity p WHERE p.userId = :userId AND p.isCompleted = true")
    long countCompletedEpisodesByUserId(@Param("userId") Long userId);

    /**
     * 统计用户观看完成的某个动漫的剧集数量
     */
    @Query("SELECT COUNT(p) FROM PlaybackProgressEntity p WHERE p.userId = :userId AND p.animeId = :animeId AND p.isCompleted = true")
    long countCompletedEpisodesByUserIdAndAnimeId(@Param("userId") Long userId, @Param("animeId") Long animeId);

    /**
     * 获取用户最近观看的动漫列表
     */
    @Query("SELECT DISTINCT p.animeId FROM PlaybackProgressEntity p WHERE p.userId = :userId ORDER BY p.lastWatchTime DESC")
    List<Long> findRecentAnimeIdsByUserId(@Param("userId") Long userId);
}
