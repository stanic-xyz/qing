package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.entities.AnimeType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Stan
 */
@Mapper
@Component
public interface AnimeTypeMapper {
    /**
     * 获取所有类型
     *
     * @return 所有类型信息
     */
    List<AnimeType> listTypes();

    /**
     * 根据ID获取类型详情
     *
     * @param id 类型ID
     * @return 类型信息
     */
    AnimeType selectTypeById(@Param("id") Long id);

    /**
     * 添加类型信息
     *
     * @param animeType 动漫类型
     */
    void addAnimeType(AnimeType animeType);

    /**
     * 删除动漫类型信息
     *
     * @param animeType 动漫类型信息
     */
    void deleteAnimeType(AnimeType animeType);
}
