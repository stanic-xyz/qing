package cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaQueryRepository;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.CategoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeCategoryJpaRepository extends BaseJpaQueryRepository<CategoryEntity, Long> {

    @Query("select (count(a) > 0) from anime_category a where a.pid=?1 and a.name = ?2")
    boolean existsByPidAndName(long pid, String name);
}
