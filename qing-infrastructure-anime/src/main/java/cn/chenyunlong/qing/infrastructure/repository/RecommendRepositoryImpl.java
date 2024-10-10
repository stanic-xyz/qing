package cn.chenyunlong.qing.infrastructure.repository;

import cn.chenyunlong.qing.domain.anime.recommend.Recommend;
import cn.chenyunlong.qing.domain.anime.recommend.repository.RecommendRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.RecommendJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendRepositoryImpl extends JpaServiceImpl<RecommendJpaRepository, Recommend, Long> implements RecommendRepository {

    private final RecommendJpaRepository recommendJpaRepository;

    @Override
    public Recommend findByAnimeId(Long animeId) {
        return recommendJpaRepository.findByAnimeId(animeId);
    }

    @Override
    public List<Recommend> queryRecommendByDateEquals(LocalDate date) {
        return List.of();
    }
}
