package cn.chenyunlong.qing.anime.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 动漫查询请求DTO
 *
 * <p>用于接收动漫查询的HTTP请求参数</p>
 *
 * @author chenyunlong
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "动漫查询请求")
public class AnimeQueryRequest {

    /**
     * 页码
     */
    @Min(value = 1, message = "页码必须大于0")
    @Schema(description = "页码", example = "1", defaultValue = "1")
    private Integer page = 1;

    /**
     * 每页大小
     */
    @Min(value = 1, message = "每页大小必须大于0")
    @Max(value = 100, message = "每页大小不能超过100")
    @Schema(description = "每页大小", example = "20", defaultValue = "20")
    private Integer size = 20;

    /**
     * 动漫名称（模糊查询）
     */
    @Size(max = 100, message = "动漫名称长度不能超过100个字符")
    @Schema(description = "动漫名称", example = "进击")
    private String name;

    /**
     * 分类ID
     */
    @Schema(description = "分类ID", example = "1")
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
     * 上架状态
     */
    @Schema(description = "上架状态", example = "true")
    private Boolean onShelf;

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
     * 排序字段
     */
    @Size(max = 50, message = "排序字段长度不能超过50个字符")
    @Schema(description = "排序字段", example = "createTime",
            allowableValues = {"createTime", "updateTime", "name", "playHeat", "orderNo"})
    private String sortBy = "createTime";

    /**
     * 排序方向
     */
    @Schema(description = "排序方向", example = "DESC",
            allowableValues = {"ASC", "DESC"})
    private String sortDirection = "DESC";

    /**
     * 是否包含已删除的记录
     */
    @Schema(description = "是否包含已删除的记录", example = "false")
    private Boolean includeDeleted = false;
}
