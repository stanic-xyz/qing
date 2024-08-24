package cn.chenyunlong.qing.infrastructure.repository.jpa;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.anime.anime.Anime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeJpaRepository extends BaseJpaRepository<Anime, Long> {

    @Query("select count(1) from Anime a where a.name = ?1")
    Integer existsByName(String name);
}
