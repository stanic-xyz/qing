package chenyunlong.zhangli.service;


import chenyunlong.zhangli.model.dto.AnimeEpisodeDTO;
import chenyunlong.zhangli.model.dto.PlayListDTO;
import chenyunlong.zhangli.model.dto.anime.AnimeInfoMinimalDTO;
import chenyunlong.zhangli.model.dto.anime.AnimeInfoUpdateDTO;
import chenyunlong.zhangli.model.entities.AnimeComment;
import chenyunlong.zhangli.model.entities.AnimeType;
import chenyunlong.zhangli.model.entities.anime.AnimeInfo;
import chenyunlong.zhangli.model.params.AnimeInfoQuery;
import chenyunlong.zhangli.model.vo.anime.AnimeInfoPlayVo;
import chenyunlong.zhangli.model.vo.anime.AnimeInfoVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.IOException;
import java.util.List;

/**
 * @author Stan
 */

public interface AnimeInfoService {

    /**
     * 获取排行榜信息
     *
     * @param pageInfo       分页信息
     * @param animeInfoQuery 查询条件
     * @return 动漫信息
     */
    IPage<AnimeInfo> getRankPage(IPage<AnimeInfo> pageInfo, AnimeInfoQuery animeInfoQuery);

    /**
     * 添加动漫信息
     *
     * @param animeInfo 动漫信息
     * @return 创建的动漫详情
     */
    AnimeInfoVo create(AnimeInfo animeInfo);

    /**
     * 获取动漫详情
     *
     * @param animeId 动漫ID
     * @return 动漫详情
     */
    AnimeInfo getById(Long animeId);

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
     * @param pageable  分页信息
     * @param animeInfo 查询参数（名称)
     * @return 满足条件的动画信息
     */
    IPage<AnimeInfoVo> listByPage(IPage<AnimeInfo> pageable, AnimeInfoQuery animeInfo);

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
     * @return 更新后的动漫详情信息
     */
    AnimeInfoVo updateBy(AnimeInfo animeInfo);

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
     * 获取更新信息
     *
     * @param page 当前页
     * @return 动漫分页信息了
     */
    IPage<AnimeInfoVo> getUpdateAnimeInfo(IPage<AnimeInfo> page);

    /**
     * 获取推荐用户列表
     *
     * @return 推荐动漫列表
     */
    List<AnimeInfoMinimalDTO> getRecommendAnimeInfoList();

    /**
     * 获取动漫的播放信息
     *
     * @param animeId 动漫ID
     * @return 动漫的所有集数
     */
    List<AnimeEpisodeDTO> getAnimeEpisodes(Long animeId);

    /**
     * 获取动漫的播放列表
     *
     * @param animeId 动漫ID
     * @return 动漫播放列表
     */
    List<PlayListDTO> getAnimePlayList(Long animeId);

    /**
     * Converts to detail vo.
     *
     * @param animeInfoDetail post must not be null
     * @return post detail vo
     */
    AnimeInfoVo convertToDetailVo(AnimeInfo animeInfoDetail);


    /**
     * Converts to play vo.
     *
     * @param animeInfoDetail post must not be null
     * @return post detail vo
     */
    AnimeInfoPlayVo convertToPlayVo(AnimeInfo animeInfoDetail);

    /**
     * 获取动漫信息
     *
     * @param animeId 动漫ID
     * @return 动漫详情
     */
    AnimeInfo getById(Integer animeId);

    /**
     * 获取最近更新的动漫列表
     *
     * @param recentPageSize 获取的动漫条数
     * @return 最近更新的动漫信息
     */
    List<AnimeInfoMinimalDTO> getRecentUpdate(int recentPageSize);

    /**
     * 下载图片
     *
     * @throws IOException 创建文件异常
     */
    void downloadImages() throws IOException;

    /**
     * 获取动漫的评论信息
     *
     * @param animeId   动漫ID
     * @param pageIndex 当前页
     * @param pageSize  分页大小
     * @return 平均信息列表
     */
    IPage<AnimeComment> getComment(Long animeId, Integer pageIndex, Integer pageSize);

    /**
     * 获取最近的动漫信息
     *
     * @return 动漫信息（最基础的信息）
     */
    List<AnimeInfoUpdateDTO> getUpdateInfo();

    /**
     * 添加评论信息
     *
     * @param cid     动漫ID
     * @param content 平均内容
     * @param user    用户名
     */
    void addComment(Long cid, String content, String user);
}
