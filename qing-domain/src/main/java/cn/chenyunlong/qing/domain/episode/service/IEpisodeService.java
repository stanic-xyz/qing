package cn.chenyunlong.qing.domain.episode.service;

import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.qing.domain.episode.dto.creator.EpisodeCreator;
import cn.chenyunlong.qing.domain.episode.dto.query.EpisodeQuery;
import cn.chenyunlong.qing.domain.episode.dto.updater.EpisodeUpdater;
import cn.chenyunlong.qing.domain.episode.dto.vo.EpisodeVO;
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

    /**
     * valid
     */
    void validEpisode(Long id);

    /**
     * invalid
     */
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
