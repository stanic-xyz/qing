package cn.chenyunlong.qing.anime.domain.anime.repository;

import cn.chenyunlong.qing.anime.domain.anime.Tag;
import cn.chenyunlong.qing.anime.domain.anime.models.TagId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

import java.util.List;

public interface TagRepository  extends BaseRepository<Tag, TagId> {

    /**
     * 判断标签名是否存在
     */
    boolean existsByName(String name);

    /**
     * 判断标签名是否不存在
     */
    boolean existsByNameAndNotId(String name, Long id);

    List<Tag> findByIds(List<Long> tagIds);
}
