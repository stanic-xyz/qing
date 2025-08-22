package cn.chenyunlong.qing.anime.domain.anime.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Schema(description = "重置播放进度请求")
@Data
public class PlaybackProgressResetRequest implements Request {

    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    @Positive(message = "用户ID必须为正数")
    private Long userId;

    @Schema(description = "剧集ID")
    @NotNull(message = "剧集ID不能为空")
    @Positive(message = "剧集ID必须为正数")
    private Long episodeId;
}