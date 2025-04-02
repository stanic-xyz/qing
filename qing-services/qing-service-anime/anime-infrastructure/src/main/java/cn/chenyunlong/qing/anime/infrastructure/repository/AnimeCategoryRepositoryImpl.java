package cn.chenyunlong.qing.anime.infrastructure.repository;


import cn.chenyunlong.qing.anime.domain.anime.Category;
import cn.chenyunlong.qing.anime.domain.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.anime.infrastructure.converter.AnimeCategoryMapper;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.CategoryEntity;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository.AnimeCategoryJpaRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnimeCategoryRepositoryImpl implements AnimeCategoryRepository {

    private final AnimeCategoryJpaRepository animeCategoryJpaRepository;

    @Override
    public Category save(Category domain) {
        CategoryEntity entity = AnimeCategoryMapper.INSTANCE.domainToEntity(domain);
        CategoryEntity category = animeCategoryJpaRepository.saveAndFlush(entity);
        domain.setId(new AggregateId(category.getId()));
        return domain;
    }

    @Override
    public Optional<Category> findById(AggregateId id) {
        if (id == null) {
            return Optional.empty();
        }
        return animeCategoryJpaRepository.findById(id.getId()).map(AnimeCategoryMapper.INSTANCE::entityToEntity);
    }

    @Override
    public boolean existsByPidAndName(Long pid, String name) {
        return animeCategoryJpaRepository.existsByPidAndName(pid == null ? Category.ROOT_PID : pid, name);
    }
}
