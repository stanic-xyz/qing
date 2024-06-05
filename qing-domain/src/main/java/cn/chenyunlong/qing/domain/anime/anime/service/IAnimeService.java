package cn.chenyunlong.qing.domain.anime.anime.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.model.AnimeCreateContext;
import cn.chenyunlong.qing.domain.anime.anime.dto.query.AnimeQuery;
import cn.chenyunlong.qing.domain.anime.anime.dto.updater.AnimeUpdater;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeVO;
import org.springframework.data.domain.Page;

public interface IAnimeService {

    /**
     * create
     */
    Long createAnime(AnimeCreateContext createContext);

    /**
     * update
     */
    void updateAnime(AnimeUpdater updater);

    /**
     * valid
     */
    void validAnime(Long id);

    /**
     * invalid
     */
    void invalidAnime(Long id);

    /**
     * 根据Id移除记录
     */
    void removeById(Long id);

    /**
     * 根据Id查询
     */
    AnimeVO findById(Long id);

    /**
     * 分页查询
     */
    Page<AnimeVO> findByPage(PageRequestWrapper<AnimeQuery> wrapper);
}
