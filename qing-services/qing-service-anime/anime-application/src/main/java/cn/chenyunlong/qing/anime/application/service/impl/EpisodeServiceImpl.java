package cn.chenyunlong.qing.anime.application.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.qing.anime.application.service.IEpisodeService;
import cn.chenyunlong.qing.anime.domain.episode.Episode;
import cn.chenyunlong.qing.anime.domain.episode.dto.creator.EpisodeCreator;
import cn.chenyunlong.qing.anime.domain.episode.dto.updater.EpisodeUpdater;
import cn.chenyunlong.qing.anime.domain.episode.dto.vo.EpisodeVO;
import cn.chenyunlong.qing.anime.domain.episode.repository.EpisodeRepository;

import cn.chenyunlong.qing.domain.base.EntityOperations;
import cn.chenyunlong.qing.anime.domain.episode.EpisodeId;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class EpisodeServiceImpl implements IEpisodeService {

    private final EpisodeRepository episodeRepository;

    /**
     * createImpl
     */
    @Override
    public Long createEpisode(EpisodeCreator creator) {
        Integer maxEpisodeNumber;
        // 设置当前最大集数
        if (Objects.isNull(creator.getEpisodeNumber())) {
            maxEpisodeNumber = episodeRepository.findMaxEpisodeNumberByPlayListId(creator.getPlayListId());
            if (maxEpisodeNumber == null || maxEpisodeNumber <= 0) {
                maxEpisodeNumber = 0;
            }
            creator.setEpisodeNumber(maxEpisodeNumber + 1);
        }
        // TODO 判断当前集数是否重复
        Episode episode = new Episode();
        episode.setAnimeId(creator.getAnimeId());
        episode.setEpisodeNumber(creator.getEpisodeNumber());
        episode.setName(creator.getName());
        episode.setDescription(creator.getDescription());
        episode.setPlayListId(creator.getPlayListId());
        episode.setPlayUrl(creator.getPlayUrl());
        Episode savedEpisode = episodeRepository.save(episode);
        return savedEpisode.getId().getValue();
    }

    /**
     * update
     */
    @Override
    public void updateEpisode(EpisodeUpdater updater) {
        EntityOperations.doUpdate(episodeRepository)
            .loadById(EpisodeId.of(updater.getId()))
            .update(updater::updateEpisode)
            .execute();
    }

    @Override
    public void validEpisode(Long id) {
        EntityOperations.doUpdate(episodeRepository)
            .loadById(EpisodeId.of(id))
            .update(BaseAggregate::valid)
            .execute();
    }

    @Override
    public void invalidEpisode(Long id) {
        EntityOperations.doUpdate(episodeRepository)
            .loadById(EpisodeId.of(id))
            .update(BaseAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public EpisodeVO findById(Long id) {
        Optional<Episode> episode = episodeRepository.findById(EpisodeId.of(id));
        return episode.map(this::entityToVo).orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError));
    }

    private EpisodeVO entityToVo(Episode episode) {
        EpisodeVO vo = new EpisodeVO();
        vo.setId(episode.getId().getValue());
        vo.setAnimeId(episode.getAnimeId());
        vo.setName(episode.getName());
        vo.setDescription(episode.getDescription());
        vo.setPlayListId(episode.getPlayListId());
        vo.setPlayUrl(episode.getPlayUrl());
        vo.setCreatedAt(episode.getCreatedAt());
        vo.setUpdatedAt(episode.getUpdatedAt());
        return vo;
    }
}
