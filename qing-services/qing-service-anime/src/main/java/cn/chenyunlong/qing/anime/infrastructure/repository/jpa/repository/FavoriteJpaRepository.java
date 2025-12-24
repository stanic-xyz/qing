package cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository;

import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteJpaRepository extends JpaRepository<FavoriteEntity, Long> {

    /**
     * 根据用户ID和动漫ID查找收藏记录
     */
    Optional<FavoriteEntity> findByUserIdAndAnimeId(Long userId, Long animeId);

    /**
     * 根据用户ID查找所有收藏记录
     */
    List<FavoriteEntity> findByUserIdOrderByFavoriteTimeDesc(Long userId);

    /**
     * 根据用户ID和收藏分组查找收藏记录
     */
    List<FavoriteEntity> findByUserIdAndFavoriteGroupOrderByFavoriteTimeDesc(Long userId, String favoriteGroup);

    /**
     * 根据用户ID查找所有收藏分组
     */
    @Query("SELECT DISTINCT f.favoriteGroup FROM FavoriteEntity f WHERE f.userId = :userId")
    List<String> findDistinctFavoriteGroupsByUserId(@Param("userId") Long userId);

    /**
     * 统计动漫的收藏数量
     */
    long countByAnimeId(Long animeId);

    /**
     * 检查用户是否收藏了某个动漫
     */
    boolean existsByUserIdAndAnimeId(Long userId, Long animeId);

    /**
     * 根据用户ID和动漫ID删除收藏记录
     */
    void deleteByUserIdAndAnimeId(Long userId, Long animeId);
}
