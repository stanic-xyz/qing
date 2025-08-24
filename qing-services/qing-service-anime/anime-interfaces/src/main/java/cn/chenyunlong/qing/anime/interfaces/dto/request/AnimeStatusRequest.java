package cn.chenyunlong.qing.anime.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


/**
 * 动漫状态操作请求DTO
 *
 * <p>用于接收动漫状态变更的HTTP请求参数</p>
 *
 * @author chenyunlong
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "动漫状态操作请求")
public class AnimeStatusRequest {

    /**
     * 操作类型
     */
    @NotNull(message = "操作类型不能为空")
    @Schema(description = "操作类型", example = "PUT_ON_SHELF", requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"PUT_ON_SHELF", "TAKE_OFF_SHELF", "DELETE", "RESTORE"})
    private String operation;

    /**
     * 操作原因（可选）
     */
    @Size(max = 500, message = "操作原因长度不能超过500个字符")
    @Schema(description = "操作原因", example = "内容违规")
    private String reason;

    /**
     * 操作人ID（可选）
     */
    @Schema(description = "操作人ID", example = "1")
    private Long operatorId;
}
