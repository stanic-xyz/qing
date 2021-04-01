package chenyunlong.zhangli.service;

import chenyunlong.zhangli.model.entities.AnimeType;

import java.util.List;

/**
 * @author Stan
 */
public interface AnimeTypeService {
    /**
     * @return 获取动漫类型信息
     */
    List<AnimeType> getAllTypeInfo();

    /**
     * @param animeType 动漫类型
     */
    void addAnimeType(AnimeType animeType);
}
