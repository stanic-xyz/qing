package cn.chenyunlong.qing.anime.infrastructure.repository;


import cn.chenyunlong.qing.anime.domain.anime.Category;
import cn.chenyunlong.qing.anime.domain.anime.repository.AnimeCategoryRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnimeCategoryRepositoryImpl implements AnimeCategoryRepository {

    @Override
    public Category save(Category entity) {
        return null;
    }

    @Override
    public Optional<Category> findById(AggregateId id) {
        return Optional.empty();
    }
}
