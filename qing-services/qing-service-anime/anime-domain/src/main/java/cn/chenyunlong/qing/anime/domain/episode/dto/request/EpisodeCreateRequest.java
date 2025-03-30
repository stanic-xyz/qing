package cn.chenyunlong.qing.anime.domain.episode.dto.request;

import cn.chenyunlong.common.annotation.FieldDesc;
import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema
@Data
public class EpisodeCreateRequest implements Request {

    @NotBlank
    @Schema(title = "name", description = "名称")
    private String name;

    private String description;

    @NotNull
    @Schema(title = "animeId", description = "动漫ID")
    private Long animeId;

    @NotNull
    @Schema(title = "collectionName", description = "播放源名称")
    private String playListId;

    @NotBlank
    @Schema(title = "playUrl", description = "播放地址")
    private String playUrl;

    @FieldDesc(name = "集数", description = "集数")
    private Integer episodeNumber;
}
