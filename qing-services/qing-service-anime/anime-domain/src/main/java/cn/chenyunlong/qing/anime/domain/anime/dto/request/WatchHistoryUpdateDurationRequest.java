package cn.chenyunlong.qing.anime.domain.anime.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Schema(description = "更新观看时长请求")
@Data
public class WatchHistoryUpdateDurationRequest implements Request {

    @Schema(description = "历史记录ID")
    @NotNull(message = "历史记录ID不能为空")
    @Positive(message = "历史记录ID必须为正数")
    private Long historyId;

    @Schema(description = "观看时长(秒)")
    @NotNull(message = "观看时长不能为空")
    @Positive(message = "观看时长必须为正数")
    private Long watchDuration;
}