package cn.chenyunlong.qing.anime.domain.anime.repository;

import cn.chenyunlong.qing.anime.domain.anime.models.Anime;
import cn.chenyunlong.qing.anime.domain.anime.models.AnimeCategory;
import cn.chenyunlong.qing.anime.domain.anime.models.AnimeId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;
import lombok.NonNull;

import java.util.List;

/**
 * 动漫聚合根仓储接口
 *
 * <p>定义动漫聚合根的持久化操作契约</p>
 *
 * <p>仓储职责：</p>
 * <ul>
 *   <li>聚合根的持久化和重建</li>
 *   <li>基于业务需求的查询操作</li>
 *   <li>确保聚合根的完整性</li>
 * </ul>
 *
 * <p>设计原则：</p>
 * <ul>
 *   <li>面向聚合根设计，不暴露内部实体</li>
 *   <li>提供业务语义明确的查询方法</li>
 *   <li>保持接口简洁，避免过度设计</li>
 * </ul>
 *
 * @author chenyunlong
 * @since 1.0.0
 */
public interface AnimeRepository extends BaseRepository<Anime, AnimeId> {

    /**
     * 根据动漫名称检查是否存在
     *
     * <p>用于创建动漫时的唯一性验证</p>
     *
     * @param name 动漫名称，不能为空
     * @return true如果存在同名动漫，false否则
     * @throws IllegalArgumentException 当名称为空时
     */
    boolean existsByName(String name);

    /**
     * 保存动漫聚合根
     *
     * <p>保存新创建的动漫或更新现有动漫</p>
     *
     * @param anime 动漫聚合根，不能为null
     * @return 保存后的动漫聚合根
     * @throws IllegalArgumentException 当动漫为null时
     */
    Anime save(@NonNull Anime anime);

    /**
     * 根据动漫ID列表批量查询
     *
     * <p>用于批量操作场景</p>
     *
     * @param animeIds 动漫ID列表
     * @return 动漫列表，不包含已删除的动漫
     */
    @NonNull
    List<Anime> findByIds(@NonNull List<Long> animeIds);

    /**
     * 根据动漫ID列表批量查询（使用AnimeId）
     *
     * @param animeIds 动漫ID列表
     * @return 动漫列表
     */
    @NonNull
    List<Anime> findByAnimeIds(@NonNull List<AnimeId> animeIds);

    /**
     * 根据分类查询动漫列表
     *
     * @param category 动漫分类
     * @return 该分类下的动漫列表
     */
    @NonNull
    List<Anime> findByCategory(@NonNull AnimeCategory category);

    /**
     * 查询已上架的动漫列表
     *
     * @return 已上架的动漫列表
     */
    @NonNull
    List<Anime> findOnShelfAnimes();

    /**
     * 根据标签查询动漫
     *
     * @param tag 标签名称
     * @return 包含该标签的动漫列表
     */
    @NonNull
    List<Anime> findByTag(@NonNull String tag);

    /**
     * 根据名称模糊查询动漫
     *
     * @param namePattern 名称模式
     * @return 匹配的动漫列表
     */
    @NonNull
    List<Anime> findByNameLike(@NonNull String namePattern);

    /**
     * 统计动漫总数
     *
     * @return 动漫总数（不包含已删除）
     */
    long countAll();

    /**
     * 统计已上架动漫数量
     *
     * @return 已上架动漫数量
     */
    long countOnShelf();

    /**
     * 根据分类统计动漫数量
     *
     * @param category 动漫分类
     * @return 该分类下的动漫数量
     */
    long countByCategory(@NonNull AnimeCategory category);

    /**
     * 删除动漫（物理删除）
     *
     * <p>注意：这是物理删除操作，请谨慎使用</p>
     *
     * @param animeId 动漫ID
     */
    void deleteById(@NonNull AnimeId animeId);

    /**
     * 批量删除动漫（物理删除）
     *
     * @param animeIds 动漫ID列表
     */
    void deleteByIds(@NonNull List<AnimeId> animeIds);

    AnimeId nextId();

    boolean existsByNameExcludeId(String cleanName, @NonNull AnimeId animeId);

    /**
     * 根据查询条件查询动漫列表
     *
     * @param query 查询条件
     * @return 符合条件的动漫列表
     */
    @NonNull
    List<Anime> findByQuery(@NonNull Object query);

    /**
     * 根据查询条件统计动漫数量
     *
     * @param query 查询条件
     * @return 符合条件的动漫数量
     */
    long countByQuery(@NonNull Object query);

    List<Anime> listAll();
}
