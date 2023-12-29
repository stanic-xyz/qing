package cn.chenyunlong.qing.domain.anime.tag.dto.creator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class TagCreator {
    @Schema(
        title = "name"
    )
    private String name;

    @Schema(
        title = "instruction"
    )
    private String instruction;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
