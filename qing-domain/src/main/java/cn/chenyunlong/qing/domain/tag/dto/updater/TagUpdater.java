package cn.chenyunlong.qing.domain.tag.dto.updater;

import cn.chenyunlong.qing.domain.tag.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import lombok.Data;

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

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
