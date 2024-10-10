package cn.chenyunlong.qing.domain.anime.anime.repository;

import cn.chenyunlong.jpa.support.BaseRepository;
import cn.chenyunlong.qing.domain.anime.anime.AnimeTagRel;

import java.util.List;

public interface AnimeTagRelRepository extends BaseRepository<AnimeTagRel, Long> {

    List<AnimeTagRel> listTagByAnimeId(Long animeId);
}
