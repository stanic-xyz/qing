package cn.chenyunlong.qing.domain.anime.type.dto.updater;

import cn.chenyunlong.qing.domain.anime.type.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Optional;

@Schema
@Data
public class TypeUpdater {

    private Long id;

    @Schema(title = "name")
    private String name;

    @Schema(title = "instruction")
    private String instruction;

    public void updateType(Type param) {
        Optional.ofNullable(getName()).ifPresent(param::setName);
        Optional.ofNullable(getInstruction()).ifPresent(param::setInstruction);
    }
}
