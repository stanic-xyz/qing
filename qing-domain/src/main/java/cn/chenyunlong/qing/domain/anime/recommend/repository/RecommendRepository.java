package cn.chenyunlong.qing.domain.anime.recommend.repository;

import cn.chenyunlong.jpa.support.BaseRepository;
import cn.chenyunlong.qing.domain.anime.recommend.Recommend;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecommendRepository extends BaseRepository<Recommend, Long> {

    Recommend findByAnimeId(
        @Param("animeId")
        Long animeId);

    @Query("select r from Recommend r where r.date= :date")
    List<Recommend> queryRecommendByDateEquals(
        @Param("date")
        LocalDate date);
}
