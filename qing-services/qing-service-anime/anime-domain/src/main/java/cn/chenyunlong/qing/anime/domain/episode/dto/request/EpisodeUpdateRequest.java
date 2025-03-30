package cn.chenyunlong.qing.anime.domain.episode.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema
@Data
public class EpisodeUpdateRequest implements Request {

    @NotNull
    private Long id;

    @NotBlank
    @Schema(title = "name", description = "名称")
    private String name;

    @Schema(title = "描述信息")
    private String description;

    @NotBlank
    private String playUrl;

    private String episodeNumber;

}
