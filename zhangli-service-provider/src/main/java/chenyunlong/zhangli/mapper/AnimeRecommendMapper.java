package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.model.entities.AnimeRecommendEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Stan
 * @date 2020/01/26
 */
@Mapper
public interface AnimeRecommendMapper {
    /**
     * 获取所有版本信息
     *
     * @return 版本信息
     */
    List<AnimeRecommendEntity> getAllVersions();

    /**
     * @param version 版本信息
     */
    void addVersionInfo(AnimeRecommendEntity version);

}
