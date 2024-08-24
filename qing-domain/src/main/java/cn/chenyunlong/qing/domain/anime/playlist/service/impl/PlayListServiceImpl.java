package cn.chenyunlong.qing.domain.anime.playlist.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.anime.anime.Anime;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeRepository;
import cn.chenyunlong.qing.domain.anime.playlist.PlayList;
import cn.chenyunlong.qing.domain.anime.playlist.dto.creator.PlayListCreator;
import cn.chenyunlong.qing.domain.anime.playlist.dto.query.PlayListQuery;
import cn.chenyunlong.qing.domain.anime.playlist.dto.updater.PlayListUpdater;
import cn.chenyunlong.qing.domain.anime.playlist.dto.vo.PlayListVO;
import cn.chenyunlong.qing.domain.anime.playlist.mapper.PlayListMapper;
import cn.chenyunlong.qing.domain.anime.playlist.repository.PlayListRepository;
import cn.chenyunlong.qing.domain.anime.playlist.service.IPlayListService;
import cn.hutool.core.lang.Assert;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class PlayListServiceImpl implements IPlayListService {

    private final PlayListRepository playListRepository;

    private final AnimeRepository animeRepository;

    /**
     * createImpl
     */
    @Override
    public Long createPlayList(PlayListCreator creator) {
        Optional<Anime> animeOptional = animeRepository.findById(creator.getAnimeId());
        if (animeOptional.isEmpty()) {
            throw new RuntimeException("动漫信息不存在");
        }
        PlayList oldPlayList = playListRepository.findByAnimeIdAndName(creator.getAnimeId(), creator.getName());
        Assert.isNull(oldPlayList, "播放列表名称已存在");
        Optional<PlayList> playList = EntityOperations.doCreate(playListRepository)
                                          .create(() -> PlayListMapper.INSTANCE.dtoToEntity(creator))
                                          .update(PlayList::init)
                                          .execute();
        return playList.isPresent() ? playList.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updatePlayList(PlayListUpdater updater) {
        EntityOperations.doUpdate(playListRepository)
            .loadById(updater.getId())
            .update(updater::updatePlayList)
            .execute();
    }

    /**
     * valid
     */
    @Override
    public void validPlayList(Long id) {
        EntityOperations.doUpdate(playListRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidPlayList(Long id) {
        EntityOperations.doUpdate(playListRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public PlayListVO findById(Long id) {
        Optional<PlayList> playList = playListRepository.findById(id);
        return playList.map(PlayListMapper.INSTANCE::entityToVo).orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError));
    }

    /**
     * findByPage
     */
    @Override
    public Page<PlayListVO> findByPage(PageRequestWrapper<PlayListQuery> query) {
        PageRequest pageRequest = PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return playListRepository.findAll(pageRequest).map(PlayListMapper.INSTANCE::entityToVo);
    }
}
