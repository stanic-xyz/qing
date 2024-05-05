package cn.chenyunlong.qing.domain.anime.anime.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.anime.anime.domainservice.model.AnimeCreateContext;
import cn.chenyunlong.qing.domain.anime.anime.dto.query.AnimeQuery;
import cn.chenyunlong.qing.domain.anime.anime.dto.updater.AnimeUpdater;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeVO;
import java.util.List;
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
     * findById
     */
    AnimeVO findById(Long id);

    /**
     * findByPage
     */
    Page<AnimeVO> findByPage(PageRequestWrapper<AnimeQuery> query);

    /**
     * 根据Id移除记录
     */
    void removeById(Long id);

    /**
     * 查询最新更新的记录
     */
    List<AnimeVO> queryLatestUpdate();
}
