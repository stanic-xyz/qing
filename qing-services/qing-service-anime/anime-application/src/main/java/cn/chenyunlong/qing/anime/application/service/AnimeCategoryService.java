package cn.chenyunlong.qing.anime.application.service;

import cn.chenyunlong.common.constants.CodeEnum;
import cn.chenyunlong.common.exception.BusinessException;
import cn.chenyunlong.common.exception.NotFoundException;
import cn.chenyunlong.qing.anime.domain.anime.Category;
import cn.chenyunlong.qing.anime.domain.anime.dto.command.AnimeCategoryCreator;
import cn.chenyunlong.qing.anime.domain.anime.dto.updater.AnimeCategoryUpdater;
import cn.chenyunlong.qing.anime.domain.anime.dto.vo.AnimeCategoryVO;
import cn.chenyunlong.qing.anime.domain.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.domain.base.EntityOperations;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnimeCategoryService {

    @Resource
    private final AnimeCategoryRepository animeCategoryRepository;

    @Transactional
    public Category createAnimeCategory(AnimeCategoryCreator creator) {
        if (animeCategoryRepository.existsByPidAndName(creator.getPid(), creator.getName())) {
            throw new IllegalArgumentException("目录已存在");
        }
        Category parent;
        if (creator.getPid() == null) {
            parent = null;
        } else {
            parent = animeCategoryRepository.findById(new AggregateId(creator.getPid()))
                .orElseThrow(() -> new NotFoundException("父级目录不存在: " + creator.getPid()));
        }
        return EntityOperations.doCreate(animeCategoryRepository)
            .create(() -> Category.create(creator.getName(), creator.getOrderNo(), parent))
            .update(Category::init)
            .execute()
            .orElseThrow(() -> new BusinessException(CodeEnum.CreateError, "创建动漫目录失败"));
    }

    /**
     * update
     */

    public void updateAnimeCategory(AnimeCategoryUpdater updater) {
        EntityOperations.doUpdate(animeCategoryRepository)
            .loadById(new AggregateId(updater.getId()))
            .update(updater::updateAnimeCategory)
            .execute();
    }


    public void validAnimeCategory(Long id) {
        EntityOperations.doUpdate(animeCategoryRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::valid)
            .execute();
    }


    public void invalidAnimeCategory(Long id) {
        EntityOperations.doUpdate(animeCategoryRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::invalid)
            .execute();
    }

    /**
     * findById
     */

    public AnimeCategoryVO findById(Long id) {
        Optional<Category> animeCategoryOptional = animeCategoryRepository.findById(new AggregateId(id));
        return animeCategoryOptional.map(this::newAnime).orElseThrow(() -> new BusinessException(CodeEnum.NotFoundError));
    }

    private AnimeCategoryVO newAnime(Category category) {
        return null;
    }
}
