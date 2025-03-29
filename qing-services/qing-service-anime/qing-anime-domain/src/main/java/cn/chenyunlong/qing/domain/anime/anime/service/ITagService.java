package cn.chenyunlong.qing.domain.anime.anime.service;

import cn.chenyunlong.qing.domain.anime.anime.dto.creator.TagCreator;

public interface ITagService {

    Long createTag(TagCreator creator);

    void validTag(Long id);

    void invalidTag(Long id);
}
