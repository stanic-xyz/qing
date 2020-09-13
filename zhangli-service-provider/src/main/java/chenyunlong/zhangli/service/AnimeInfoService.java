package chenyunlong.zhangli.service;


import chenyunlong.zhangli.entities.anime.AnimeInfo;
import chenyunlong.zhangli.model.response.anime.AnimeInfoRankModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Stan
 */

public interface AnimeInfoService {

    AnimeInfoRankModel getRankPage(Pageable pageable);

    void add(List<AnimeInfo> animeInfos);

    AnimeInfo getMovieDetail(String movieId);
}
