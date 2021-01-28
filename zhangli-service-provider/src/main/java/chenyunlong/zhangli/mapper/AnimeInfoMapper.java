package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.entities.anime.AnimeInfo;
import chenyunlong.zhangli.model.param.AnimeQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Stan
 * @date 2020/11/01
 */
@Mapper
@Component
public interface AnimeInfoMapper {

    /**
     * 分页获取动画信息
     *
     * @param pageable 分页信息
     * @return 所有动画的分页信息
     */
    List<AnimeInfo> listAnimes(@Param("page") Pageable pageable);

    /**
     * 计算满足条件的记录个数
     *
     * @return 总数
     */
    Long count();

    /**
     * 添加动画
     *
     * @param anime 动画信息
     */
    void insert(@Param("anime") AnimeInfo anime);

    /**
     * 获取动漫详情
     *
     * @param movieId 动漫ID
     * @return 具体的动画信息
     */
    AnimeInfo selectAnimationDetail(Long movieId);

    /**
     * 根据动画名称查询动画信息
     *
     * @param animeQuery 动画名称
     * @param offset     偏差值
     * @param pageSize   分页大小
     * @return 返回
     */
    List<AnimeInfo> selectAnimationW(@Param("anime") AnimeQuery animeQuery, Long offset, Integer pageSize);

    /**
     * 根据名称查询动漫信息
     *
     * @param name 动漫名称
     * @return 满足条件的动漫信息
     */
    long countByNameLike(String name);

    /**
     * 更新动漫信息
     *
     * @param animeInfo 动漫信息
     */
    void update(AnimeInfo animeInfo);

    /**
     * 根据ID删除动漫信息
     *
     * @param animeId 动漫ID
     */
    void deleteByAnimeId(Long animeId);

    /**
     * 获取连载中的动漫总数
     *
     * @return 连载中的动漫总数
     */
    long getUpdateAnimeCount();

    /**
     * @param pageRequest 分页对象
     * @return 连载中的动漫信息
     */
    List<AnimeInfo> selectAnimeByUpdateTime(@Param("pageRequest") Pageable pageRequest);
}
