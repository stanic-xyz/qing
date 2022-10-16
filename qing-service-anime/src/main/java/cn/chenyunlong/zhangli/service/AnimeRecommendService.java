package cn.chenyunlong.zhangli.service;

import cn.chenyunlong.zhangli.model.entities.AnimeRecommendEntity;
import cn.chenyunlong.zhangli.model.params.AnimeInfoQuery;
import cn.chenyunlong.zhangli.model.params.AnimeRecommendParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Stan
 */
public interface AnimeRecommendService {
    /**
     * 分页获取推荐信息
     *
     * @param pageable 分页信息
     * @return 分页
     */
    List<AnimeRecommendEntity> getRecommendAnimeInfoList(Pageable pageable, AnimeInfoQuery animeInfoQuery);

    /**
     * 获取推荐详情信息
     *
     * @param recommendId 推荐ID
     * @return 推荐信息
     */
    AnimeRecommendEntity getById(Long recommendId);

    /**
     * 添加推荐信息
     *
     * @param recommendEntity 推荐信息
     * @return 推荐信息
     */
    AnimeRecommendEntity addRecommend(AnimeRecommendEntity recommendEntity);

    /**
     * 添加推荐信息
     *
     * @param typeParam 推荐信息
     * @param pageable  分页信息
     * @return 推荐信息分页
     */
    IPage<AnimeRecommendEntity> pageBy(AnimeRecommendParam typeParam, Pageable pageable);

    AnimeRecommendEntity update(AnimeRecommendEntity animeRecommendEntity);
}
