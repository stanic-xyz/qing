package cn.chenyunlong.qing.anime.domain.anime.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Schema(description = "添加收藏请求")
@Data
public class FavoriteAddRequest implements Request {

    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    @Positive(message = "用户ID必须为正数")
    private Long userId;

    @Schema(description = "动漫ID")
    @NotNull(message = "动漫ID不能为空")
    @Positive(message = "动漫ID必须为正数")
    private Long animeId;

    @Schema(description = "收藏分组")
    private String favoriteGroup;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "是否公开")
    private Boolean isPublic;
}
