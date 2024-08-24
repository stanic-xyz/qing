package cn.chenyunlong.qing.infrastructure.repository.jpa;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.anime.playlist.PlayList;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayListJpaRepository extends BaseJpaRepository<PlayList, Long> {

    @Query("select p from PlayList p where p.animeId = ?1 and p.name = ?2")
    PlayList findByAnimeIdAndName(Long animeId, String name);
}
