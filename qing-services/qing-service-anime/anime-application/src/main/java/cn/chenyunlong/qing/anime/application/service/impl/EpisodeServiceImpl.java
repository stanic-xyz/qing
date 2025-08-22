package cn.chenyunlong.qing.anime.application.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.qing.anime.application.service.IEpisodeService;
import cn.chenyunlong.qing.anime.domain.episode.Episode;
import cn.chenyunlong.qing.anime.domain.episode.dto.creator.EpisodeCreator;
import cn.chenyunlong.qing.anime.domain.episode.dto.updater.EpisodeUpdater;
import cn.chenyunlong.qing.anime.domain.episode.dto.vo.EpisodeVO;
import cn.chenyunlong.qing.anime.domain.episode.repository.EpisodeRepository;
import cn.chenyunlong.qing.anime.infrastructure.converter.EpisodeMapper;
import cn.chenyunlong.qing.domain.base.EntityOperations;
import cn.chenyunlong.qing.domain.common.AggregateId;
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
        Episode episode = EpisodeMapper.INSTANCE.dtoToEntity(creator);
        Episode savedEpisode = episodeRepository.save(episode);
        return savedEpisode.getId().getId();
    }

    /**
     * update
     */
    @Override
    public void updateEpisode(EpisodeUpdater updater) {
        EntityOperations.doUpdate(episodeRepository)
            .loadById(new AggregateId(updater.getId()))
            .update(updater::updateEpisode)
            .execute();
    }

    @Override
    public void validEpisode(Long id) {
        EntityOperations.doUpdate(episodeRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::valid)
            .execute();
    }

    @Override
    public void invalidEpisode(Long id) {
        EntityOperations.doUpdate(episodeRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public EpisodeVO findById(Long id) {
        Optional<Episode> episode = episodeRepository.findById(new AggregateId(id));
        return episode.map(this::entityToVo).orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError));
    }

    private EpisodeVO entityToVo(Episode episode) {
        return EpisodeMapper.INSTANCE.entityToVo(episode);
    }
}
