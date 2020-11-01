package chenyunlong.zhangli.mapper;

import chenyunlong.zhangli.entities.anime.AnimeInfo;
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
     * @param animeInfos 动画信息列表
     */
    void insertPatch(@Param("animeInfos") List<AnimeInfo> animeInfos);

    /**
     * 获取动漫详情
     *
     * @param movieId 动漫ID
     * @return 具体的动画信息
     */
    AnimeInfo selectAnimationDetail(String movieId);

    /**
     * 根据动画名称查询动画信息
     *
     * @param animeName 动画名称
     * @param offset    偏差值
     * @param pageSize  分页大小
     * @return 返回
     */
    List<AnimeInfo> selectAnimationW(String animeName, Integer offset, Integer pageSize);

    /**
     * 根据名称查询动漫信息
     *
     * @param name 动漫名称
     * @return 满足条件的动漫信息
     */
    long countByName(String name);
}
