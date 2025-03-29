package cn.chenyunlong.qing.domain.anime.anime.dto.creator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimeCategoryCreator {

    @Schema(
        title = "name"
    )
    private String name;

    @Schema(
        title = "orderNo"
    )
    private Integer orderNo;

    @Schema(
        title = "pid"
    )
    private Long pid;


}
