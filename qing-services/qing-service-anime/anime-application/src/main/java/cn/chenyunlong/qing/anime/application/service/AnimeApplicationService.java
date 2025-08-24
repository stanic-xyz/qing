package cn.chenyunlong.qing.anime.application.service;

import java.util.List;
import java.util.Optional;

import cn.chenyunlong.qing.anime.application.command.AnimeStatusCommand;
import cn.chenyunlong.qing.anime.application.command.CreateAnimeCommand;
import cn.chenyunlong.qing.anime.application.command.UpdateAnimeCommand;
import cn.chenyunlong.qing.anime.application.dto.AnimeDTO;
import cn.chenyunlong.qing.anime.application.dto.PageResult;
import cn.chenyunlong.qing.anime.application.query.AnimeQuery;
import lombok.NonNull;

/**
 * 动漫应用服务接口
 * 
 * <p>
 * 定义动漫相关的应用服务操作，协调领域对象完成业务用例
 * </p>
 * 
 * <p>
 * 职责：
 * </p>
 * <ul>
 * <li>处理应用层的业务用例</li>
 * <li>协调领域对象完成复杂的业务操作</li>
 * <li>管理事务边界</li>
 * <li>发布领域事件</li>
 * <li>进行数据转换和验证</li>
 * </ul>
 * 
 * @author chenyunlong
 * @since 1.0.0
 */
public interface AnimeApplicationService {

    /**
     * 创建动漫
     * 
     * <p>
     * 创建新的动漫实体，包括以下步骤：
     * </p>
     * <ul>
     * <li>验证命令参数</li>
     * <li>检查动漫名称是否重复</li>
     * <li>验证分类和标签的有效性</li>
     * <li>创建动漫聚合根</li>
     * <li>保存到仓储</li>
     * <li>发布领域事件</li>
     * </ul>
     * 
     * @param command 创建动漫命令
     * @return 创建的动漫DTO
     * @throws IllegalArgumentException 当命令参数无效时
     * @throws IllegalStateException    当动漫名称已存在时
     */
    AnimeDTO createAnime(@NonNull CreateAnimeCommand command);

    /**
     * 更新动漫信息
     * 
     * <p>
     * 更新现有动漫的信息，包括以下步骤：
     * </p>
     * <ul>
     * <li>验证命令参数</li>
     * <li>加载动漫聚合根</li>
     * <li>检查动漫名称是否重复（如果名称有变更）</li>
     * <li>验证标签的有效性</li>
     * <li>更新动漫信息</li>
     * <li>保存到仓储</li>
     * <li>发布领域事件</li>
     * </ul>
     * 
     * @param command 更新动漫命令
     * @return 更新后的动漫DTO
     * @throws IllegalArgumentException 当命令参数无效时
     * @throws IllegalStateException    当动漫不存在或名称已存在时
     */
    AnimeDTO updateAnime(@NonNull UpdateAnimeCommand command);

    /**
     * 执行动漫状态操作
     * 
     * <p>
     * 执行动漫的状态变更操作，包括上架、下架、删除、恢复等
     * </p>
     * 
     * @param command 状态操作命令
     * @return 操作后的动漫DTO
     * @throws IllegalArgumentException 当命令参数无效时
     * @throws IllegalStateException    当动漫不存在或操作不允许时
     */
    AnimeDTO executeStatusOperation(@NonNull AnimeStatusCommand command);

    /**
     * 根据ID查询动漫
     * 
     * @param animeId 动漫ID
     * @return 动漫DTO，如果不存在则返回空
     * @throws IllegalArgumentException 当动漫ID无效时
     */
    Optional<AnimeDTO> findAnimeById(@NonNull Long animeId);

    /**
     * 根据ID列表批量查询动漫
     * 
     * @param animeIds 动漫ID列表
     * @return 动漫DTO列表
     * @throws IllegalArgumentException 当动漫ID列表无效时
     */
    List<AnimeDTO> findAnimesByIds(@NonNull List<Long> animeIds);

    /**
     * 分页查询动漫
     * 
     * <p>
     * 根据查询条件分页查询动漫列表
     * </p>
     * 
     * @param query 查询条件
     * @return 分页结果
     * @throws IllegalArgumentException 当查询条件无效时
     */
    PageResult<AnimeDTO> findAnimes(@NonNull AnimeQuery query);

    /**
     * 查询上架的动漫
     * 
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
    PageResult<AnimeDTO> findOnShelfAnimes(Integer page, Integer size);

    /**
     * 根据分类查询动漫
     * 
     * @param categoryId 分类ID
     * @param page       页码
     * @param size       每页大小
     * @return 分页结果
     * @throws IllegalArgumentException 当分类ID无效时
     */
    PageResult<AnimeDTO> findAnimesByCategory(@NonNull Long categoryId, Integer page, Integer size);

    /**
     * 根据标签查询动漫
     * 
     * @param tagId 标签ID
     * @param page  页码
     * @param size  每页大小
     * @return 分页结果
     * @throws IllegalArgumentException 当标签ID无效时
     */
    PageResult<AnimeDTO> findAnimesByTag(@NonNull Long tagId, Integer page, Integer size);

    /**
     * 根据名称模糊查询动漫
     * 
     * @param name 动漫名称
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     * @throws IllegalArgumentException 当名称无效时
     */
    PageResult<AnimeDTO> findAnimesByName(@NonNull String name, Integer page, Integer size);

    /**
     * 统计动漫总数
     * 
     * @return 动漫总数
     */
    Long countAllAnimes();

    /**
     * 统计上架动漫数量
     * 
     * @return 上架动漫数量
     */
    Long countOnShelfAnimes();

    /**
     * 统计指定分类的动漫数量
     * 
     * @param categoryId 分类ID
     * @return 动漫数量
     * @throws IllegalArgumentException 当分类ID无效时
     */
    Long countAnimesByCategory(@NonNull Long categoryId);

    /**
     * 检查动漫名称是否存在
     * 
     * @param name 动漫名称
     * @return true如果存在，false否则
     * @throws IllegalArgumentException 当名称无效时
     */
    Boolean existsByName(@NonNull String name);

    /**
     * 检查动漫名称是否存在（排除指定ID）
     * 
     * @param name      动漫名称
     * @param excludeId 排除的动漫ID
     * @return true如果存在，false否则
     * @throws IllegalArgumentException 当参数无效时
     */
    Boolean existsByNameExcludeId(@NonNull String name, @NonNull Long excludeId);
}
