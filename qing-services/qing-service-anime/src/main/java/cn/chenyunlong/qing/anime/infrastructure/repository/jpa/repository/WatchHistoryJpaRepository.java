package cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository;

import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.WatchHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WatchHistoryJpaRepository extends JpaRepository<WatchHistoryEntity, Long> {

    /**
     * 根据用户ID查找观看历史，按观看时间倒序
     */
    List<WatchHistoryEntity> findByUserIdOrderByWatchTimeDesc(Long userId);

    /**
     * 根据用户ID和动漫ID查找观看历史
     */
    List<WatchHistoryEntity> findByUserIdAndAnimeIdOrderByWatchTimeDesc(Long userId, Long animeId);

    /**
     * 根据用户ID和剧集ID查找观看历史
     */
    List<WatchHistoryEntity> findByUserIdAndEpisodeIdOrderByWatchTimeDesc(Long userId, Long episodeId);

    /**
     * 根据时间范围查找用户观看历史
     */
    List<WatchHistoryEntity> findByUserIdAndWatchTimeBetweenOrderByWatchTimeDesc(
            Long userId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计用户观看总时长
     */
    @Query("SELECT SUM(w.watchDuration) FROM WatchHistoryEntity w WHERE w.userId = :userId")
    Long sumWatchDurationByUserId(@Param("userId") Long userId);

    /**
     * 统计用户观看某个动漫的总时长
     */
    @Query("SELECT SUM(w.watchDuration) FROM WatchHistoryEntity w WHERE w.userId = :userId AND w.animeId = :animeId")
    Long sumWatchDurationByUserIdAndAnimeId(@Param("userId") Long userId, @Param("animeId") Long animeId);

    /**
     * 获取用户最近观看的动漫列表
     */
    @Query("SELECT DISTINCT w.animeId FROM WatchHistoryEntity w WHERE w.userId = :userId ORDER BY w.watchTime DESC")
    List<Long> findRecentAnimeIdsByUserId(@Param("userId") Long userId);

    /**
     * 统计动漫的观看次数
     */
    long countByAnimeId(Long animeId);

    /**
     * 根据用户ID删除观看历史
     */
    void deleteByUserId(Long userId);
}
