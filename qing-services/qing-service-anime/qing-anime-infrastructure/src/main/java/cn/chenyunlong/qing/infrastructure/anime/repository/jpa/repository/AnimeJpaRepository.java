package cn.chenyunlong.qing.infrastructure.anime.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.infrastructure.anime.repository.jpa.entity.AnimeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeJpaRepository extends BaseJpaRepository<AnimeEntity, Long> {

    @Query("select exists (select 1 from AnimeEntity a where a.name = ?1)")
    boolean existsByName(String name);
}
