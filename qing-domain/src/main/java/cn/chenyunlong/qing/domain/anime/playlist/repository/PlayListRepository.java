package cn.chenyunlong.qing.domain.anime.playlist.repository;

import cn.chenyunlong.jpa.support.BaseRepository;
import cn.chenyunlong.qing.domain.anime.playlist.PlayList;
import java.util.List;

public interface PlayListRepository extends BaseRepository<PlayList, Long> {

    PlayList findByAnimeIdAndName(Long animeId, String name);

    List<PlayList> listByAnime(Long animeId);
}
