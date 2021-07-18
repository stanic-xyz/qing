package chenyunlong.zhangli.common.service;

import chenyunlong.zhangli.model.entities.AnimeType;
import chenyunlong.zhangli.model.params.AnimeTypeParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.data.domain.Pageable;

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
     * @return 动漫信息
     */
    AnimeType addAnimeType(AnimeType animeType);

    /**
     * 获取动漫类型详情
     *
     * @param typeId 动漫类型ID
     * @return 动漫类型信息
     */
    AnimeType getById(Long typeId);

    /**
     * 更新动漫信息
     *
     * @param typeInfo 类型信息
     * @return 更新后的类型信息
     */
    AnimeType update(AnimeType typeInfo);

    /**
     * 分页获取类型信息
     *
     * @param typeParam 类型参数信息
     * @param pageable  分页信息
     * @return 分页信息
     */
    IPage<AnimeType> pageBy(AnimeTypeParam typeParam, Pageable pageable);
}
