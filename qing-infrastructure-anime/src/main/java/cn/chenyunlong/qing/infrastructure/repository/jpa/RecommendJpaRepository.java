package cn.chenyunlong.qing.infrastructure.repository.jpa;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.anime.recommend.Recommend;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecommendJpaRepository extends BaseJpaRepository<Recommend, Long> {

    @Query("select r from Recommend r where r.animeId = :animeId")
    Recommend findByAnimeId(
        @Param("animeId")
        Long animeId);

    @Query("select r from Recommend r where r.date= :date")
    List<Recommend> queryRecommendByDateEquals(
        @Param("date")
        LocalDate date);
}
