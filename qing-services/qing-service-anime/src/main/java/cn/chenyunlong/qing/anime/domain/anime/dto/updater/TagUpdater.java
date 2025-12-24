package cn.chenyunlong.qing.anime.domain.anime.dto.updater;

import cn.chenyunlong.qing.anime.domain.anime.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

import java.util.Optional;

@Getter
@Schema
@Data
public class TagUpdater {

    @Schema(
        title = "name"
    )
    private String name;

    @Schema(
        title = "instruction"
    )
    private String instruction;

    private Long id;

    public void updateTag(Tag param) {
        Optional.ofNullable(getName()).ifPresent(param::setName);
        Optional.ofNullable(getInstruction()).ifPresent(param::setInstruction);
    }
}
