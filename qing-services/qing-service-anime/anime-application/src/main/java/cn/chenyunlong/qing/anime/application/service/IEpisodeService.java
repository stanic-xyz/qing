package cn.chenyunlong.qing.anime.application.service;

import cn.chenyunlong.qing.anime.domain.episode.dto.creator.EpisodeCreator;
import cn.chenyunlong.qing.anime.domain.episode.dto.updater.EpisodeUpdater;
import cn.chenyunlong.qing.anime.domain.episode.dto.vo.EpisodeVO;

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

}
