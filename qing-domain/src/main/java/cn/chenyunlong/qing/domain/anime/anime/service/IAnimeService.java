package cn.chenyunlong.qing.domain.anime.anime.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.anime.anime.dto.creator.AnimeCreator;
import cn.chenyunlong.qing.domain.anime.anime.dto.query.AnimeQuery;
import cn.chenyunlong.qing.domain.anime.anime.dto.updater.AnimeUpdater;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeDetailVO;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeVO;
import org.springframework.data.domain.Page;

public interface IAnimeService {

    /**
     * create
     */
    Long createAnime(AnimeCreator creator);

    /**
     * update
     */
    void updateAnime(AnimeUpdater updater);

    void validAnime(Long id);

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
     * 根据Id查询
     */
    AnimeDetailVO findDetailById(Long id);

    /**
     * 分页查询
     */
    Page<AnimeVO> findByPage(PageRequestWrapper<AnimeQuery> wrapper);
}
