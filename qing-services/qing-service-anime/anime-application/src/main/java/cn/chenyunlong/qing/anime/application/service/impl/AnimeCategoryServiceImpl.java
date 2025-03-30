package cn.chenyunlong.qing.anime.application.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.qing.anime.application.service.IAnimeCategoryService;
import cn.chenyunlong.qing.anime.domain.anime.Category;
import cn.chenyunlong.qing.anime.domain.anime.dto.creator.AnimeCategoryCreator;
import cn.chenyunlong.qing.anime.domain.anime.dto.updater.AnimeCategoryUpdater;
import cn.chenyunlong.qing.anime.domain.anime.dto.vo.AnimeCategoryVO;
import cn.chenyunlong.qing.anime.domain.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.domain.base.EntityOperations;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class AnimeCategoryServiceImpl implements IAnimeCategoryService {

    @Resource
    private final AnimeCategoryRepository animeCategoryRepository;

    /**
     * createImpl
     */
    @Override
    public Long createAnimeCategory(AnimeCategoryCreator creator) {
        Optional<Category> animeCategory = EntityOperations.doCreate(animeCategoryRepository)
            .create(Category::new)
            .update(Category::init)
            .execute();
        return animeCategory.isPresent() ? animeCategory.get().getAggregateId().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateAnimeCategory(AnimeCategoryUpdater updater) {
        EntityOperations.doUpdate(animeCategoryRepository)
            .loadById(new AggregateId(updater.getId()))
            .update(updater::updateAnimeCategory)
            .execute();
    }

    @Override
    public void validAnimeCategory(Long id) {
        EntityOperations.doUpdate(animeCategoryRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::valid)
            .execute();
    }

    @Override
    public void invalidAnimeCategory(Long id) {
        EntityOperations.doUpdate(animeCategoryRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public AnimeCategoryVO findById(Long id) {
        Optional<Category> animeCategoryOptional = animeCategoryRepository.findById(new AggregateId(id));
        return animeCategoryOptional.map(this::newAnime).orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError));
    }

    private AnimeCategoryVO newAnime(Category category) {
        return null;
    }
}
