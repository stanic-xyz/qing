package cn.chenyunlong.qing.anime.domain.anime.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Schema(description = "记录观看历史请求")
@Data
public class WatchHistoryRecordRequest implements Request {

    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    @Positive(message = "用户ID必须为正数")
    private Long userId;

    @Schema(description = "动漫ID")
    @NotNull(message = "动漫ID不能为空")
    @Positive(message = "动漫ID必须为正数")
    private Long animeId;

    @Schema(description = "剧集ID")
    @NotNull(message = "剧集ID不能为空")
    @Positive(message = "剧集ID必须为正数")
    private Long episodeId;

    @Schema(description = "观看时长(秒)")
    @NotNull(message = "观看时长不能为空")
    @Positive(message = "观看时长必须为正数")
    private Long duration;

    @Schema(description = "设备类型")
    private String deviceType;
}