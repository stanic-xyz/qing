package cn.chenyunlong.qing.infrastructure.repository;

import cn.chenyunlong.qing.domain.anime.playlist.PlayList;
import cn.chenyunlong.qing.domain.anime.playlist.repository.PlayListRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.PlayListJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayListRepositoryImpl extends JpaServiceImpl<PlayListJpaRepository, PlayList, Long> implements PlayListRepository {

    private final PlayListJpaRepository playListJpaRepository;

    @Override
    public PlayList findByAnimeIdAndName(Long animeId, String name) {
        return playListJpaRepository.findByAnimeIdAndName(animeId, name);
    }

    @Override
    public List<PlayList> listByAnime(Long animeId) {
        return playListJpaRepository.listByAnime(animeId);
    }
}
