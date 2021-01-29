package chenyunlong.zhangli.service;


import chenyunlong.zhangli.entities.AnimeType;
import chenyunlong.zhangli.entities.anime.AnimeInfo;
import chenyunlong.zhangli.model.param.AnimeQuery;
import chenyunlong.zhangli.model.vo.anime.AnimeInfoRankModel;
import chenyunlong.zhangli.model.vo.anime.AnimeInfoVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Stan
 */

public interface AnimeInfoService {

    AnimeInfoRankModel getRankPage(Pageable pageable, AnimeQuery animeQuery);

    /**
     * 添加动漫信息
     *
     * @param animeInfo 动漫信息
     */
    void add(AnimeInfo animeInfo);

    /**
     * 获取动漫详情
     *
     * @param movieId 动漫ID
     * @return 动漫详情
     */
    AnimeInfoVo getMovieDetail(Long movieId);

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
     *
     * @param pageable
     * @param animeInfo 查询参数（名称)
     * @return 满足条件的动画信息
     */
    List<AnimeInfo> query(Pageable pageable, AnimeQuery animeInfo);

    /**
     * 获取播放页数据
     *
     * @param animeId 动漫ID
     * @param type    播放类型
     * @param ep      分级序号
     * @return 动漫播放页视图
     */
    AnimeInfoVo getPlayDetail(Long animeId, int type, int ep);

    /**
     * 更新动漫信息
     *
     * @param animeInfo 动漫信息
     */
    void updateAnime(AnimeInfo animeInfo);

    /**
     * 删除动漫信息
     *
     * @param animeId 动漫ID
     */
    void deleteAnime(Long animeId);

    /**
     * 获取所有的类型信息
     *
     * @return 类型信息
     */
    List<AnimeType> getAllAnimeType();

    /**
     * 添加动漫类型信息
     *
     * @param animeType 动漫类型
     * @return 动漫类型信息
     */
    AnimeType addAnimeType(AnimeType animeType);

    /**
     * @param page     当前页
     * @param pageSize 分页大小
     * @return 动漫分页信息了
     */
    Page<AnimeInfo> getUpdateAnimeInfo(Integer page, Integer pageSize);
}
