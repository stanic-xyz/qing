package cn.chenyunlong.qing.anime.infrastructure.repository;

import java.util.Optional;

import org.springframework.stereotype.Service;

import cn.chenyunlong.qing.anime.domain.anime.Category;
import cn.chenyunlong.qing.anime.domain.anime.models.CategoryId;
import cn.chenyunlong.qing.anime.domain.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.anime.infrastructure.converter.AnimeCategoryInfrastructureMapper;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.CategoryEntity;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository.AnimeCategoryJpaRepository;
import jakarta.annotation.Resource;
import lombok.NonNull;

@Service
public class AnimeCategoryRepositoryImpl implements AnimeCategoryRepository {

    @Resource
    private AnimeCategoryJpaRepository animeCategoryJpaRepository;
    @Resource
    private AnimeCategoryInfrastructureMapper animeCategoryInfrastructureMapper;

    public AnimeCategoryRepositoryImpl(AnimeCategoryJpaRepository animeCategoryJpaRepository) {
        this.animeCategoryJpaRepository = animeCategoryJpaRepository;
    }

    @Override
    public Category save(Category domain) {
        CategoryEntity entity = animeCategoryInfrastructureMapper.domainToEntity(domain);
        CategoryEntity category = animeCategoryJpaRepository.saveAndFlush(entity);
        domain.setId(CategoryId.of(category.getId()));
        return domain;
    }

    @Override
    public Optional<Category> findById(CategoryId id) {
        if (id == null) {
            return Optional.empty();
        }
        return animeCategoryJpaRepository.findById(id.getValue())
                .map(animeCategoryInfrastructureMapper::entityToEntity);
    }

    @Override
    public boolean existsByPidAndName(Long pid, String name) {
        return animeCategoryJpaRepository.existsByPidAndName(pid == null ? Category.ROOT_PID : pid, name);
    }

    @Override
    public boolean existsById(@NonNull Long categoryId) {
        return animeCategoryJpaRepository.existsById(categoryId);
    }
}
