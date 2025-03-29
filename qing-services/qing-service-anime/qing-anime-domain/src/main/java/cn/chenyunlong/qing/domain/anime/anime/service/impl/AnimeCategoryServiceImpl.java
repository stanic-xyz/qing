package cn.chenyunlong.qing.domain.anime.anime.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.qing.domain.anime.anime.Category;
import cn.chenyunlong.qing.domain.anime.anime.dto.creator.AnimeCategoryCreator;
import cn.chenyunlong.qing.domain.anime.anime.dto.updater.AnimeCategoryUpdater;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeCategoryVO;
import cn.chenyunlong.qing.domain.anime.anime.mapper.AnimeCategoryMapper;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.domain.anime.anime.service.IAnimeCategoryService;
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
            .create(() -> AnimeCategoryMapper.INSTANCE.dtoToEntity(creator))
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
        return animeCategoryOptional.map(AnimeCategoryMapper.INSTANCE::entityToVo).orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError));
    }
}
