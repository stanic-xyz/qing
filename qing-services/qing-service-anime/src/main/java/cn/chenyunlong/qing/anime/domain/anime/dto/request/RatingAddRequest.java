package cn.chenyunlong.qing.anime.domain.anime.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Schema(description = "添加评分评论请求")
@Data
public class RatingAddRequest implements Request {

    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    @Positive(message = "用户ID必须为正数")
    private Long userId;

    @Schema(description = "动漫ID")
    @NotNull(message = "动漫ID不能为空")
    @Positive(message = "动漫ID必须为正数")
    private Long animeId;

    @Schema(description = "评分(1-10)")
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最小值为1")
    @Max(value = 10, message = "评分最大值为10")
    private Integer score;

    @Schema(description = "评论内容")
    private String comment;

    @Schema(description = "是否匿名")
    private Boolean isAnonymous;

    @Schema(description = "是否公开")
    private Boolean isPublic;
}
