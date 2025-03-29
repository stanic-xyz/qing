package cn.chenyunlong.qing.domain.anime.episode.service;

import cn.chenyunlong.qing.domain.anime.episode.dto.creator.EpisodeCreator;
import cn.chenyunlong.qing.domain.anime.episode.dto.updater.EpisodeUpdater;
import cn.chenyunlong.qing.domain.anime.episode.dto.vo.EpisodeVO;

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
