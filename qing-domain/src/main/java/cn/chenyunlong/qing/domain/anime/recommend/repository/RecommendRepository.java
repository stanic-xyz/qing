package cn.chenyunlong.qing.domain.anime.recommend.repository;

import cn.chenyunlong.jpa.support.BaseRepository;
import cn.chenyunlong.qing.domain.anime.recommend.Recommend;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecommendRepository extends BaseRepository<Recommend, Long> {

    @Query("select r from Recommend r where r.animeId = :animeId")
    Recommend findByAnimeId(@Param("animeId") Long animeId);
}
