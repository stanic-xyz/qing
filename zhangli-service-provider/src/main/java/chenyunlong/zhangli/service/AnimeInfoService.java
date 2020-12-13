package chenyunlong.zhangli.service;


import chenyunlong.zhangli.entities.anime.AnimeInfo;
import chenyunlong.zhangli.model.vo.anime.AnimeInfoRankModel;
import chenyunlong.zhangli.model.vo.anime.AnimeInfoVo;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Stan
 */

public interface AnimeInfoService {

    AnimeInfoRankModel getRankPage(Pageable pageable);

    void add(AnimeInfo animeInfos);

    /**
     * 获取动漫详情
     *
     * @param movieId 动漫ID
     * @return 动漫详情
     */
    AnimeInfoVo getMovieDetail(String movieId);

    /**
     * 获取查询总数
     *
     * @param query 查询条件
     * @return 总数
     */
    long getTotalCount(String query);

    /**
     * 查询动画信息
     *
     * @param query    查询参数（名称
     * @param page     当前页
     * @param pageSize 分页大小
     * @return 满足条件的动画信息
     */
    List<AnimeInfo> query(String query, Integer page, Integer pageSize);

    /**
     * 获取播放页数据
     *
     * @param animeId 动漫ID
     * @return 动漫播放页视图
     */
    AnimeInfoVo getPlayDetail(String animeId);

    void updateAnime(AnimeInfo animeModel);

    /**
     * 删除动漫信息
     *
     * @param animeId 动漫ID
     */
    void deleteAnime(Long animeId);
}
