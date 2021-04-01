package chenyunlong.zhangli.service;

import chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import chenyunlong.zhangli.model.params.AnimeInfoQuery;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Stan
 */
public interface AnimeRecommendService {
    /**
     * 分页获取推荐信息
     *
     * @param pageable 分页信息
     * @return
     */
    List<AnimeInfo> getRecommendAnimeInfoList(Pageable pageable, AnimeInfoQuery animeInfoQuery);
}
