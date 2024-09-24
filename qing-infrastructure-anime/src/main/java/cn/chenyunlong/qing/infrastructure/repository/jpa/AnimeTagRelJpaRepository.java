package cn.chenyunlong.qing.infrastructure.repository.jpa;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.anime.anime.AnimeTagRel;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeTagRelJpaRepository extends BaseJpaRepository<AnimeTagRel, Long> {

    @Query("select count(1) from Anime a where a.name = ?1")
    Integer existsByName(String name);

    @Query("select r from AnimeTagRel r where r.animeId = ?1")
    List<AnimeTagRel> listTagByAnimeId(Long animeId);
}