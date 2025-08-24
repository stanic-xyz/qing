package cn.chenyunlong.qing.anime.domain.anime.repository;

import cn.chenyunlong.qing.anime.domain.anime.Category;
import cn.chenyunlong.qing.anime.domain.anime.models.CategoryId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

public interface AnimeCategoryRepository extends BaseRepository<Category, CategoryId> {

    boolean existsByPidAndName(Long pid, String name);

    boolean existsById(Long categoryId);
}
