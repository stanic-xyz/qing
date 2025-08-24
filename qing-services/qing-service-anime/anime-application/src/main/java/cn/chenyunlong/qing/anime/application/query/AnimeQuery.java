package cn.chenyunlong.qing.anime.application.query;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 动漫查询条件
 *
 * <p>用于动漫查询的条件对象，支持多种查询条件组合</p>
 *
 * @author chenyunlong
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimeQuery {

    /**
     * 动漫ID列表
     */
    private List<Long> animeIds;

    /**
     * 动漫名称（模糊查询）
     */
    private String name;

    /**
     * 分类ID
     * -- GETTER --
     *  获取分类ID
     *
     * @return 分类ID

     */
    private Long categoryId;

    /**
     * 标签ID
     */
    private List<Long> tagIds;

    /**
     * 播放状态
     */
    private String playStatus;

    /**
     * 是否上架（null表示不限制）
     */
    private Boolean onShelf;

    /**
     * 是否删除（null表示不限制）
     */
    private Boolean deleted;

    /**
     * 页码（从1开始）
     */
    @Builder.Default
    private Integer page = 1;

    /**
     * 每页大小
     */
    @Builder.Default
    private Integer size = 20;

    /**
     * 排序字段
     */
    @Builder.Default
    private String sortBy = "createTime";

    /**
     * 排序方向
     */
    @Builder.Default
    private SortDirection sortDirection = SortDirection.DESC;

    /**
     * 排序方向枚举
     */
    public enum SortDirection {
        /**
         * 升序
         */
        ASC,

        /**
         * 降序
         */
        DESC
    }

    /**
     * 验证查询条件的有效性
     *
     * @throws IllegalArgumentException 当查询条件无效时
     */
    public void validate() {
        if (page != null && page < 1) {
            throw new IllegalArgumentException("页码必须大于等于1");
        }

        if (size != null && (size < 1 || size > 100)) {
            throw new IllegalArgumentException("每页大小必须在1-100之间");
        }

        if (animeIds != null && animeIds.size() > 100) {
            throw new IllegalArgumentException("动漫ID列表不能超过100个");
        }

        if (name != null && name.length() > 100) {
            throw new IllegalArgumentException("动漫名称查询条件长度不能超过100个字符");
        }
    }

    /**
     * 检查是否有查询条件
     *
     * @return true如果有查询条件，false否则
     */
    public boolean hasConditions() {
        return (animeIds != null && !animeIds.isEmpty()) ||
                StringUtils.hasText(name) ||
                categoryId != null ||
                tagIds != null ||
                StringUtils.hasText(playStatus) ||
                onShelf != null ||
                deleted != null;
    }

    /**
     * 获取清理后的名称查询条件
     *
     * @return 清理后的名称
     */
    public String getCleanName() {
        return name != null ? name.trim() : null;
    }

    /**
     * /**
     * 获取偏移量
     *
     * @return 偏移量
     */
    public int getOffset() {
        return (page - 1) * size;
    }

    /**
     * 创建查询所有动漫的查询条件
     *
     * @return 查询条件
     */
    public static AnimeQuery all() {
        return AnimeQuery.builder().build();
    }

    /**
     * 创建查询上架动漫的查询条件
     *
     * @return 查询条件
     */
    public static AnimeQuery onShelf() {
        AnimeQuery query = new AnimeQuery();
        query.onShelf = true;
        query.deleted = false;
        return query;
    }

    /**
     * 创建按分类查询的查询条件
     *
     * @param categoryId 分类ID
     * @return 查询条件
     */
    public static AnimeQuery byCategory(Long categoryId) {
        AnimeQuery query = new AnimeQuery();
        query.categoryId = categoryId;
        query.deleted = false;
        return query;
    }

    /**
     * 创建按标签查询的查询条件
     *
     * @param tagId 标签ID
     * @return 查询条件
     */
    public static AnimeQuery byTag(Long tagId) {
        AnimeQuery query = new AnimeQuery();
        query.tagIds = CollUtil.toList(tagId);
        query.deleted = false;
        return query;
    }
}
