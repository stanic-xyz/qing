package cn.chenyunlong.qing.anime.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 创建动漫请求DTO
 *
 * <p>用于接收创建动漫的HTTP请求参数</p>
 *
 * @author chenyunlong
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "创建动漫请求")
public class AnimeCreateRequest {

    /**
     * 动漫名称
     */
    @NotBlank(message = "动漫名称不能为空")
    @Size(max = 100, message = "动漫名称长度不能超过100个字符")
    @Schema(description = "动漫名称", example = "进击的巨人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    /**
     * 动漫简介
     */
    @Size(max = 2000, message = "动漫简介长度不能超过2000个字符")
    @Schema(description = "动漫简介", example = "一部关于巨人的动漫作品")
    private String instruction;

    /**
     * 动漫分类ID
     */
    @NotNull(message = "动漫分类ID不能为空")
    @Schema(description = "动漫分类ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long categoryId;

    /**
     * 标签ID列表
     */
    @Schema(description = "标签ID列表", example = "[1, 2, 3]")
    private List<Long> tagIds;

    /**
     * 播放状态
     */
    @Size(max = 50, message = "播放状态长度不能超过50个字符")
    @Schema(description = "播放状态", example = "连载中")
    private String playStatus;

    /**
     * 封面图片URL
     */
    @Size(max = 500, message = "封面图片URL长度不能超过500个字符")
    @Schema(description = "封面图片URL", example = "https://example.com/cover.jpg")
    private String coverUrl;

    /**
     * 原始名称
     */
    @Size(max = 100, message = "原始名称长度不能超过100个字符")
    @Schema(description = "原始名称", example = "進撃の巨人")
    private String originalName;

    /**
     * 其他名称
     */
    @Size(max = 200, message = "其他名称长度不能超过200个字符")
    @Schema(description = "其他名称", example = "Attack on Titan")
    private String otherName;

    /**
     * 作者
     */
    @Size(max = 100, message = "作者名称长度不能超过100个字符")
    @Schema(description = "作者", example = "諫山創")
    private String author;

    /**
     * 官方网站
     */
    @Size(max = 500, message = "官方网站URL长度不能超过500个字符")
    @Schema(description = "官方网站", example = "https://shingeki.tv/")
    private String officialWebsite;

    /**
     * 地区
     */
    @Size(max = 50, message = "地区名称长度不能超过50个字符")
    @Schema(description = "地区", example = "日本")
    private String district;

    /**
     * 公司ID
     */
    @Schema(description = "制作公司ID", example = "1")
    private Long companyId;

    /**
     * 类型ID
     */
    @Schema(description = "动漫类型ID", example = "1")
    private Long typeId;

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
    @Size(max = 50, message = "播放热度长度不能超过50个字符")
    @Schema(description = "播放热度", example = "9.8")
    private String playHeat;

    /**
     * 排序号
     */
    @Schema(description = "排序号", example = "1")
    private Integer orderNo;
}
