package cn.chenyunlong.qing.infrastructure.repository;

import cn.chenyunlong.qing.domain.anime.anime.AnimeTagRel;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeTagRelRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.AnimeTagRelJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnimeTagRelRepositoryImpl extends JpaServiceImpl<AnimeTagRelJpaRepository, AnimeTagRel, Long> implements AnimeTagRelRepository {

    private final AnimeTagRelJpaRepository animeTagRelJpaRepository;

    @Override
    public List<AnimeTagRel> listTagByAnimeId(Long animeId) {
        return animeTagRelJpaRepository.listTagByAnimeId(animeId);
    }
}
