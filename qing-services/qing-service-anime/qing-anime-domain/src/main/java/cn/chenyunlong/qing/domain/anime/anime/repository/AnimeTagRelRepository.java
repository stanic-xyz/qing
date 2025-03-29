package cn.chenyunlong.qing.domain.anime.anime.repository;

import cn.chenyunlong.qing.domain.anime.anime.AnimeTagRel;

import java.util.List;

public interface AnimeTagRelRepository {

    List<AnimeTagRel> listTagByAnimeId(Long animeId);
}
