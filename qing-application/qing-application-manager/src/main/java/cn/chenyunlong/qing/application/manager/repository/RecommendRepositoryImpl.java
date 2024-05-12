package cn.chenyunlong.qing.application.manager.repository;

import cn.chenyunlong.qing.application.manager.repository.jpa.RecommendJpaRepository;
import cn.chenyunlong.qing.domain.anime.recommend.Recommend;
import cn.chenyunlong.qing.domain.anime.recommend.repository.RecommendRepository;
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
