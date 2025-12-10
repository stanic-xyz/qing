package cn.chenyunlong.qing.anime.application.dto;

import cn.chenyunlong.qing.anime.domain.anime.models.Anime;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 动漫数据传输对象
 *
 * <p>
 * 用于应用层和外部层之间传输动漫数据
 * </p>
 *
 * @author chenyunlong
 * @since 1.0.0
 */
@Data
@Builder
public class AnimeDTO {

    /**
     * 动漫ID
     */
    private Long id;

    /**
     * 动漫名称
     */
    private String name;

    /**
     * 动漫简介
     */
    private String instruction;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 播放状态
     */
    private String playStatus;

    /**
     * 封面图片URL
     */
    private String coverUrl;

    /**
     * 原始名称
     */
    private String originalName;

    /**
     * 其他名称
     */
    private String otherName;

    /**
     * 作者
     */
    private String author;

    /**
     * 官方网站
     */
    private String officialWebsite;

    /**
     * 地区
     */
    private String district;

    /**
     * 公司ID
     */
    private Long companyId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 类型ID
     */
    private Long typeId;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 是否上架
     */
    private Boolean onShelf;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime lastUpdateTime;

    /**
     * 创建者ID
     */
    private Long createdBy;

    /**
     * 最后更新者ID
     */
    private Long lastUpdatedBy;

    /**
     * 版本号（用于乐观锁）
     */
    private Long version;

    /**
     * 检查动漫是否处于活跃状态
     *
     * @return true如果动漫处于活跃状态，false否则
     */
    public boolean isActive() {
        return Boolean.TRUE.equals(onShelf) && !Boolean.TRUE.equals(deleted);
    }

    /**
     * 检查动漫是否有标签
     *
     * @return true如果有标签，false否则
     */
    public boolean hasTags() {
        return tags != null && !tags.isEmpty();
    }

    /**
     * 获取标签数量
     *
     * @return 标签数量
     */
    public int getTagCount() {
        return tags != null ? tags.size() : 0;
    }

    /**
     * 检查是否包含指定标签
     *
     * @param tag 标签名称
     * @return true如果包含指定标签，false否则
     */
    public boolean hasTag(String tag) {
        return tags != null && tags.contains(tag);
    }

    /**
     * 获取显示名称（优先使用name，如果为空则使用originalName）
     *
     * @return 显示名称
     */
    public String getDisplayName() {
        return name != null && !name.trim().isEmpty() ? name : originalName;
    }

    /**
     * 获取状态描述
     *
     * @return 状态描述
     */
    public String getStatusDescription() {
        if (Boolean.TRUE.equals(deleted)) {
            return "已删除";
        }
        if (Boolean.TRUE.equals(onShelf)) {
            return "已上架";
        }
        return "未上架";
    }

    /**
     * 从动漫聚合根创建DTO
     *
     * @param anime 动漫聚合根
     * @return 动漫DTO
     */
    public static AnimeDTO from(Anime anime) {
        if (anime == null) {
            return null;
        }

        return AnimeDTO.builder()
                .id(anime.getId().id())
                .name(anime.getName())
                .instruction(anime.getInstruction())
                .categoryId(anime.getAnimeCategory() != null ? anime.getAnimeCategory().id().id() : null)
                .categoryName(anime.getAnimeCategory() != null ? anime.getAnimeCategory().name() : null)
                .tags(anime.getTags() != null ? anime.getTags().asList() : null)
                .playStatus(anime.getPlayStatus() != null ? anime.getPlayStatus().name() : null)
                .coverUrl(anime.getCover())
                .originalName(anime.getOriginalName())
                .otherName(anime.getOtherName())
                .author(anime.getAuthor())
                .officialWebsite(anime.getOfficialWebsite())
                .district(anime.getDistrict() != null ? anime.getDistrict().getName() : null)
                .companyId(anime.getCompany() != null ? anime.getCompany().companyId() : null)
                .companyName(anime.getCompany() != null ? anime.getCompany().companyName() : null)
                .typeId(anime.getType() != null ? anime.getType().typeId().id() : null)
                .typeName(anime.getType() != null ? anime.getType().typeName() : null)
                .onShelf(anime.isOnShelf())
                .deleted(anime.isDeleted())
                .lastUpdateTime(anime.getLastUpdateTime())
                .lastUpdatedBy(anime.getLastUpdatedBy())
                .build();
    }
}
