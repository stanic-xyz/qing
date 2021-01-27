package chenyunlong.zhangli.service;

import chenyunlong.zhangli.entities.anime.AnimeInfo;
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
    List<AnimeInfo> getRecommendAnimeInfoList(Pageable pageable);
}
