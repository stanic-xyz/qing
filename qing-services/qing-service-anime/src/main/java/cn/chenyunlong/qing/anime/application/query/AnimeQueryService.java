package cn.chenyunlong.qing.anime.application.query;

import cn.chenyunlong.qing.anime.application.dto.AnimeDTO;
import cn.chenyunlong.qing.anime.application.dto.PageResult;
import cn.chenyunlong.qing.anime.domain.anime.models.AnimeId;

import java.util.List;
import java.util.Optional;

/**
 * 动漫查询服务接口
 * 负责处理所有动漫相关的查询操作，遵循CQRS模式
 *
 * @author chenyunlong
 * @since 2024-12-30
 */
public interface AnimeQueryService {

    /**
     * 根据ID查询动漫
     *
     * @param animeId 动漫ID
     * @return 动漫DTO，如果不存在则返回空
     */
    Optional<AnimeDTO> findById(AnimeId animeId);

    /**
     * 根据ID列表批量查询动漫
     *
     * @param animeIds 动漫ID列表
     * @return 动漫DTO列表
     */
    List<AnimeDTO> findByIds(List<AnimeId> animeIds);

    /**
     * 分页查询动漫
     *
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<AnimeDTO> findPage(AnimeQuery query);

    /**
     * 查询上架的动漫
     *
     * @param page 页码（从1开始）
     * @param size 每页大小
     * @return 分页结果
     */
    PageResult<AnimeDTO> findOnShelf(int page, int size);

    /**
     * 根据分类查询动漫
     *
     * @param categoryId 分类ID
     * @param page       页码（从1开始）
     * @param size       每页大小
     * @return 分页结果
     */
    PageResult<AnimeDTO> findByCategory(Long categoryId, int page, int size);

    /**
     * 根据标签查询动漫
     *
     * @param tagId 标签ID
     * @param page  页码（从1开始）
     * @param size  每页大小
     * @return 分页结果
     */
    PageResult<AnimeDTO> findByTag(Long tagId, int page, int size);

    /**
     * 根据名称模糊查询动漫
     *
     * @param name 动漫名称（支持模糊匹配）
     * @param page 页码（从1开始）
     * @param size 每页大小
     * @return 分页结果
     */
    PageResult<AnimeDTO> findByNameLike(String name, int page, int size);

    /**
     * 统计动漫总数
     *
     * @return 动漫总数
     */
    long countTotal();

    /**
     * 统计上架动漫数量
     *
     * @return 上架动漫数量
     */
    long countOnShelf();

    /**
     * 统计指定分类的动漫数量
     *
     * @param categoryId 分类ID
     * @return 该分类的动漫数量
     */
    long countByCategory(Long categoryId);

    /**
     * 检查动漫名称是否存在
     *
     * @param name 动漫名称
     * @return 如果存在返回true，否则返回false
     */
    boolean existsByName(String name);

    /**
     * 检查动漫名称是否存在（排除指定ID）
     *
     * @param name      动漫名称
     * @param excludeId 要排除的动漫ID
     * @return 如果存在返回true，否则返回false
     */
    boolean existsByNameExcludeId(String name, AnimeId excludeId);

    /**
     * 获取热门动漫
     *
     * @param limit 限制数量
     * @return 热门动漫列表
     */
    List<AnimeDTO> findPopular(int limit);

    /**
     * 获取最新动漫
     *
     * @param limit 限制数量
     * @return 最新动漫列表
     */
    List<AnimeDTO> findLatest(int limit);

    /**
     * 获取推荐动漫
     *
     * @param limit 限制数量
     * @return 推荐动漫列表
     */
    List<AnimeDTO> findRecommended(int limit);

    List<AnimeDTO> listAll();
}
