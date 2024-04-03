package cn.chenyunlong.qing.domain.anime.anime.repository;

import cn.chenyunlong.jpa.support.BaseRepository;
import cn.chenyunlong.qing.domain.anime.anime.Tag;
import org.springframework.data.jpa.repository.Query;

public interface TagRepository extends BaseRepository<Tag, Long> {

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
