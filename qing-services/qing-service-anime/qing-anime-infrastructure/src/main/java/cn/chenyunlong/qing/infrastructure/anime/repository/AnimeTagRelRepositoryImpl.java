package cn.chenyunlong.qing.infrastructure.anime.repository;

import cn.chenyunlong.qing.domain.anime.anime.AnimeTagRel;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeTagRelRepository;
import cn.chenyunlong.qing.infrastructure.anime.repository.jpa.repository.AnimeTagRelJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeTagRelRepositoryImpl implements AnimeTagRelRepository {

    private final AnimeTagRelJpaRepository animeTagRelJpaRepository;

    @Override
    public List<AnimeTagRel> listTagByAnimeId(Long animeId) {
        return animeTagRelJpaRepository.listTagByAnimeId(animeId);
    }

}
