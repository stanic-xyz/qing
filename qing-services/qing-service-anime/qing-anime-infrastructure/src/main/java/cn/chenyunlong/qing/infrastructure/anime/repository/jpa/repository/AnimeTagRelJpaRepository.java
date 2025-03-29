package cn.chenyunlong.qing.infrastructure.anime.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaQueryRepository;
import cn.chenyunlong.qing.domain.anime.anime.AnimeTagRel;
import cn.chenyunlong.qing.infrastructure.anime.repository.jpa.entity.AnimeTagRelEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimeTagRelJpaRepository extends BaseJpaQueryRepository<AnimeTagRelEntity, Long> {


    @Query("select r from AnimeTagRelEntity r where r.animeId = ?1")
    List<AnimeTagRel> listTagByAnimeId(Long animeId);
}
