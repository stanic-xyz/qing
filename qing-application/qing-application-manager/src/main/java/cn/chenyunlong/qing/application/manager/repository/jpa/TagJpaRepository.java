package cn.chenyunlong.qing.application.manager.repository.jpa;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.anime.anime.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagJpaRepository extends BaseJpaRepository<Tag, Long> {

    /**
     * 判断标签名是否存在
     */
    @Query("select exists(select t from Tag t where t.name = ?1) ")
    boolean existsByName(String name);

    /**
     * 判断标签名是否不存在
     */
    @Query("select exists(select t from Tag t where t.name = ?1 and t.id != ?2)")
    boolean existsByNameAndNotId(String name, Long id);
}
