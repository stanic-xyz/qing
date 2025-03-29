package cn.chenyunlong.qing.infrastructure.anime.repository;


import cn.chenyunlong.qing.domain.anime.anime.Category;
import cn.chenyunlong.qing.domain.anime.anime.repository.AnimeCategoryRepository;
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
