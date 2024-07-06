package cn.chenyunlong.qing.infrastructure.repository;

import cn.chenyunlong.qing.domain.anime.recommend.Recommend;
import cn.chenyunlong.qing.domain.anime.recommend.repository.RecommendRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.RecommendJpaRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RecommendRepositoryImpl extends JpaServiceImpl<RecommendJpaRepository, Recommend, Long> implements RecommendRepository {


    @Override
    public Recommend findByAnimeId(Long animeId) {
        return null;
    }

    @Override
    public List<Recommend> queryRecommendByDateEquals(LocalDate date) {
        return List.of();
    }
}
