package cn.chenyunlong.qing.anime.application.service;

import cn.chenyunlong.qing.anime.domain.type.dto.creator.TypeCreator;

public interface ITypeService {

    Long createType(TypeCreator creator);

    void validType(Long id);

    void invalidType(Long id);
}
