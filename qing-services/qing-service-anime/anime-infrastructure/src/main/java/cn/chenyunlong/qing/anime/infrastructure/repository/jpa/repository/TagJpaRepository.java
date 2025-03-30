package cn.chenyunlong.qing.anime.infrastructure.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.anime.infrastructure.repository.jpa.entity.TagEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagJpaRepository extends BaseJpaRepository<TagEntity, Long> {

    /**
     * 判断标签名是否存在
     */
    @Query("select exists(select t from TagEntity t where t.name = ?1) ")
    boolean existsByName(String name);

    /**
     * 判断标签名是否不存在
     */
    @Query("select exists(select t from TagEntity t where t.name = ?1 and t.id != ?2)")
    boolean existsByNameAndNotId(String name, Long id);
}
