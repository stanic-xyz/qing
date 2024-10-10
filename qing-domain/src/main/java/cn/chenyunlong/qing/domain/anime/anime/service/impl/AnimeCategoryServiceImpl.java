package cn.chenyunlong.qing.domain.anime.anime.service.impl;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.model.PageRequestWrapper;
import cn.chenyunlong.jpa.support.EntityOperations;
import cn.chenyunlong.jpa.support.domain.BaseEntity;
import cn.chenyunlong.qing.domain.anime.anime.AnimeCategory;
import cn.chenyunlong.qing.domain.anime.anime.dto.creator.AnimeCategoryCreator;
import cn.chenyunlong.qing.domain.anime.anime.dto.query.AnimeCategoryQuery;
import cn.chenyunlong.qing.domain.anime.anime.dto.updater.AnimeCategoryUpdater;
import cn.chenyunlong.qing.domain.anime.anime.dto.vo.AnimeCategoryVO;
import cn.chenyunlong.qing.domain.anime.anime.mapper.AnimeCategoryMapper;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.domain.anime.anime.service.IAnimeCategoryService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        Optional<AnimeCategory> animeCategory = EntityOperations.doCreate(animeCategoryRepository)
            .create(() -> AnimeCategoryMapper.INSTANCE.dtoToEntity(creator))
            .update(AnimeCategory::init)
            .execute();
        return animeCategory.isPresent() ? animeCategory.get().getId() : 0;
    }

    /**
     * update
     */
    @Override
    public void updateAnimeCategory(AnimeCategoryUpdater updater) {
        EntityOperations.doUpdate(animeCategoryRepository)
            .loadById(updater.getId())
            .update(updater::updateAnimeCategory)
            .execute();
    }

    @Override
    public void validAnimeCategory(Long id) {
        EntityOperations.doUpdate(animeCategoryRepository)
            .loadById(id)
            .update(BaseEntity::valid)
            .execute();
    }

    @Override
    public void invalidAnimeCategory(Long id) {
        EntityOperations.doUpdate(animeCategoryRepository)
            .loadById(id)
            .update(BaseEntity::invalid)
            .execute();
    }

    /**
     * findById
     */
    @Override
    public AnimeCategoryVO findById(Long id) {
        Optional<AnimeCategory> animeCategoryOptional = animeCategoryRepository.findById(id);
        return animeCategoryOptional.map(AnimeCategoryMapper.INSTANCE::entityToVo).orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError));
    }

    /**
     * 分页查询
     */
    @Override
    public Page<AnimeCategoryVO> findByPage(PageRequestWrapper<AnimeCategoryQuery> wrapper) {
        PageRequest pageRequest =
            PageRequest.of(wrapper.getPage(), wrapper.getPageSize(), Sort.Direction.DESC, "createdAt");
        return animeCategoryRepository.findAll(pageRequest).map(AnimeCategoryMapper.INSTANCE::entityToVo);
    }
}
