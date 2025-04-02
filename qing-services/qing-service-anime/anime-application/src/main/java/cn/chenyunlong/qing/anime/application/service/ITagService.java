package cn.chenyunlong.qing.anime.application.service;

import cn.chenyunlong.qing.anime.domain.anime.dto.command.TagCreator;

public interface ITagService {

    Long createTag(TagCreator creator);

    void validTag(Long id);

    void invalidTag(Long id);
}
