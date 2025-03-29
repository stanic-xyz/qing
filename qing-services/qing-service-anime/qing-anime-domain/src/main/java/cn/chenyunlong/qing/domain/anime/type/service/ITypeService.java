package cn.chenyunlong.qing.domain.anime.type.service;

import cn.chenyunlong.qing.domain.anime.type.dto.creator.TypeCreator;
import cn.chenyunlong.qing.domain.anime.type.dto.updater.TypeUpdater;

public interface ITypeService {

    Long createType(TypeCreator creator);

    void validType(Long id);

    void invalidType(Long id);
}
