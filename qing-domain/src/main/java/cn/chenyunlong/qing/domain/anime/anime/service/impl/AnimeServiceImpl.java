package cn.chenyunlong.qing.domain.anime.anime.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.BaseJpaAggregate;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.qing.domain.anime.anime.Anime;
import cn.chenyunlong.qing.domain.anime.anime.AnimeCategory;
import cn.chenyunlong.qing.domain.anime.anime.dto.creator.AnimeCreator;
import cn.chenyunlong.qing.domain.anime.anime.dto.query.AnimeQuery;
import cn.chenyunlong.qing.domain.anime.anime.dto.updater.AnimeUpdater;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeVO;
import cn.chenyunlong.qing.domain.anime.anime.mapper.AnimeMapper;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeRepository;
import cn.chenyunlong.qing.domain.anime.anime.service.IAnimeService;
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
public class AnimeServiceImpl implements IAnimeService {

    private final AnimeRepository animeRepository;
    private final AnimeCategoryRepository categoryRepository;

    /**
     * createImpl
     */
    @Override
    public Long createAnime(AnimeCreator creator) {
        Optional<Anime> anime = EntityOperations.doCreate(animeRepository)
                                    .create(() -> {
                                        AnimeCategory animeCategory = categoryRepository.getReferenceById(creator.getTypeId());
                                        Assert.notNull(animeCategory, "分类信息不存在");
                                        Anime entity = AnimeMapper.INSTANCE.dtoToEntity(creator);
                                        entity.setTypeName(animeCategory.getName());
                                        return entity;
                                    })
                                    .update(Anime::init)
                                    .execute();
        return anime.isPresent() ? anime.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateAnime(AnimeUpdater updater) {
        EntityOperations.doUpdate(animeRepository)
            .loadById(updater.getId())
            .update(updater::updateAnime)
            .execute();
    }

    /**
     * valid
     */
    @Override
    public void validAnime(Long id) {
        EntityOperations.doUpdate(animeRepository)
            .loadById(id)
            .update(BaseJpaAggregate::valid)
            .execute();
    }

    /**
     * invalid
     */
    @Override
    public void invalidAnime(Long id) {
        EntityOperations.doUpdate(animeRepository)
            .loadById(id)
            .update(BaseJpaAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public AnimeVO findById(Long id) {
        Optional<Anime> anime = animeRepository.findById(id);
        return anime.map(AnimeMapper.INSTANCE::entityToVo).orElseThrow(() -> new BusinessException(CodeEnum.NotFindError));
    }

    /**
     * findByPage
     */
    @Override
    public Page<AnimeVO> findByPage(PageRequestWrapper<AnimeQuery> query) {
        PageRequest pageRequest =
            PageRequest.of(query.getPage(), query.getPageSize(), Sort.Direction.DESC, "createdAt");
        return animeRepository.findAll(pageRequest).map(AnimeMapper.INSTANCE::entityToVo);
    }
}
