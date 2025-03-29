package cn.chenyunlong.qing.infrastructure.anime.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaQueryRepository;
import cn.chenyunlong.qing.domain.anime.anime.Category;
import cn.chenyunlong.qing.infrastructure.anime.repository.jpa.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeCategoryJpaRepository extends BaseJpaQueryRepository<CategoryEntity, Long> {

}
