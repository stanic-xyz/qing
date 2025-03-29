package cn.chenyunlong.qing.domain.anime.anime.repository;

import cn.chenyunlong.qing.domain.anime.anime.Tag;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;

import java.util.List;

public interface TagRepository  extends BaseRepository<Tag, AggregateId> {

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
