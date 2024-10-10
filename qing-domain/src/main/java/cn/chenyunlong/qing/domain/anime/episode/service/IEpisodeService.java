package cn.chenyunlong.qing.domain.anime.episode.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.anime.episode.dto.creator.EpisodeCreator;
import cn.chenyunlong.qing.domain.anime.episode.dto.query.EpisodeQuery;
import cn.chenyunlong.qing.domain.anime.episode.dto.updater.EpisodeUpdater;
import cn.chenyunlong.qing.domain.anime.episode.dto.vo.EpisodeVO;
import org.springframework.data.domain.Page;

public interface IEpisodeService {

    /**
     * create
     */
    Long createEpisode(EpisodeCreator creator);

    /**
     * update
     */
    void updateEpisode(EpisodeUpdater updater);

    void validEpisode(Long id);

    void invalidEpisode(Long id);

    /**
     * findById
     */
    EpisodeVO findById(Long id);

    /**
     * findByPage
     */
    Page<EpisodeVO> findByPage(PageRequestWrapper<EpisodeQuery> query);
}
