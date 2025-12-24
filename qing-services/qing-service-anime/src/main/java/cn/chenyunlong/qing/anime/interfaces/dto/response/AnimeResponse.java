package cn.chenyunlong.qing.anime.interfaces.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 动漫响应DTO
 * 
 * <p>用于返回动漫信息的HTTP响应对象</p>
 * 
 * @author chenyunlong
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "动漫响应")
public class AnimeResponse {

    /**
     * 动漫ID
     */
    @Schema(description = "动漫ID", example = "1")
    private Long id;

    /**
     * 动漫名称
     */
    @Schema(description = "动漫名称", example = "进击的巨人")
    private String name;

    /**
     * 动漫简介
     */
    @Schema(description = "动漫简介", example = "一部关于巨人的动漫作品")
    private String instruction;

    /**
     * 分类ID
     */
    @Schema(description = "分类ID", example = "1")
    private Long categoryId;

    /**
     * 分类名称
     */
    @Schema(description = "分类名称", example = "热血")
    private String categoryName;

    /**
     * 标签列表
     */
    @Schema(description = "标签列表", example = "[\"动作\", \"剧情\"]")
    private List<String> tags;

    /**
     * 播放状态
     */
    @Schema(description = "播放状态", example = "连载中")
    private String playStatus;

    /**
     * 封面图片URL
     */
    @Schema(description = "封面图片URL", example = "https://example.com/cover.jpg")
    private String cover;

    /**
     * 原始名称
     */
    @Schema(description = "原始名称", example = "進撃の巨人")
    private String originalName;

    /**
     * 其他名称
     */
    @Schema(description = "其他名称", example = "Attack on Titan")
    private String otherName;

    /**
     * 作者
     */
    @Schema(description = "作者", example = "諫山創")
    private String author;

    /**
     * 官方网站
     */
    @Schema(description = "官方网站", example = "https://shingeki.tv/")
    private String officialWebsite;

    /**
     * 地区
     */
    @Schema(description = "地区", example = "日本")
    private String district;

    /**
     * 公司ID
     */
    @Schema(description = "制作公司ID", example = "1")
    private Long companyId;

    /**
     * 公司名称
     */
    @Schema(description = "制作公司名称", example = "WIT STUDIO")
    private String companyName;

    /**
     * 类型ID
     */
    @Schema(description = "动漫类型ID", example = "1")
    private Long typeId;

    /**
     * 类型名称
     */
    @Schema(description = "动漫类型名称", example = "TV动画")
    private String typeName;

    /**
     * 首播日期
     */
    @Schema(description = "首播日期", example = "2013-04-07")
    private String premiereDate;

    /**
     * 剧情类型列表
     */
    @Schema(description = "剧情类型列表", example = "[\"动作\", \"剧情\"]")
    private List<String> plotTypes;

    /**
     * 播放热度
     */
    @Schema(description = "播放热度", example = "9.8")
    private String playHeat;

    /**
     * 排序号
     */
    @Schema(description = "排序号", example = "1")
    private Integer orderNo;

    /**
     * 上架状态
     */
    @Schema(description = "上架状态", example = "true")
    private Boolean onShelf;

    /**
     * 删除状态
     */
    @Schema(description = "删除状态", example = "false")
    private Boolean deleted;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2024-01-01T10:00:00")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", example = "2024-01-01T10:00:00")
    private LocalDateTime updateTime;

    /**
     * 创建者ID
     */
    @Schema(description = "创建者ID", example = "1")
    private Long createUserId;

    /**
     * 更新者ID
     */
    @Schema(description = "更新者ID", example = "1")
    private Long updateUserId;

    /**
     * 版本号
     */
    @Schema(description = "版本号", example = "1")
    private Long version;

    /**
     * 是否激活状态
     * 
     * @return 是否激活
     */
    @Schema(description = "是否激活状态", example = "true")
    public Boolean isActive() {
        return onShelf != null && onShelf && (deleted == null || !deleted);
    }

    /**
     * 是否有标签
     * 
     * @return 是否有标签
     */
    @Schema(description = "是否有标签", example = "true")
    public Boolean hasTags() {
        return tags != null && !tags.isEmpty();
    }

    /**
     * 获取标签数量
     * 
     * @return 标签数量
     */
    @Schema(description = "标签数量", example = "3")
    public Integer getTagCount() {
        return tags != null ? tags.size() : 0;
    }

    /**
     * 检查是否包含指定标签
     * 
     * @param tag 标签名称
     * @return 是否包含
     */
    public Boolean hasTag(String tag) {
        return tags != null && tags.contains(tag);
    }

    /**
     * 获取显示名称（优先显示中文名称）
     * 
     * @return 显示名称
     */
    @Schema(description = "显示名称", example = "进击的巨人")
    public String getDisplayName() {
        if (name != null && !name.trim().isEmpty()) {
            return name;
        }
        if (originalName != null && !originalName.trim().isEmpty()) {
            return originalName;
        }
        if (otherName != null && !otherName.trim().isEmpty()) {
            return otherName;
        }
        return "未知";
    }

    /**
     * 获取状态描述
     * 
     * @return 状态描述
     */
    @Schema(description = "状态描述", example = "已上架")
    public String getStatusDescription() {
        if (deleted != null && deleted) {
            return "已删除";
        }
        if (onShelf != null && onShelf) {
            return "已上架";
        }
        return "已下架";
    }
}
