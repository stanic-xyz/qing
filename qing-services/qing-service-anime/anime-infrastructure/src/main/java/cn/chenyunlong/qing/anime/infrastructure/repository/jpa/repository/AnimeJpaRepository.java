package cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaQueryRepository;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.AnimeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimeJpaRepository extends BaseJpaQueryRepository<AnimeEntity, Long> {

    @Query("select exists (select 1 from AnimeEntity a where a.name = ?1)")
    boolean existsByName(String name);

    Page<AnimeEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("select a from AnimeEntity a where a.typeId = ?1")
    List<AnimeEntity> findByCategoryId(Long categoryId);

    boolean existsByNameAndId(String name, Long id);

    boolean existsByNameAndIsDeletedFalse(String name);

    boolean existsByNameAndIdNotAndIsDeletedFalse(String name, Long id);

    long countByIsDeletedFalse();

    long countByTypeIdAndIsDeletedFalse(Long typeId);

    /**
     * 查找上架且未删除的动漫
     *
     * @param pageable 分页参数
     * @return 动漫分页结果
     */
    Page<AnimeEntity> findByIsOnShelfTrueAndIsDeletedFalse(Pageable pageable);

    long countByIsOnShelfTrueAndIsDeletedFalse();
}
