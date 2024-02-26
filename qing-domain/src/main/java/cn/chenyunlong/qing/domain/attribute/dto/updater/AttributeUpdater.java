package cn.chenyunlong.qing.domain.attribute.dto.updater;

import cn.chenyunlong.qing.domain.attribute.Attribute;
import cn.chenyunlong.qing.domain.attribute.WebControlType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import lombok.Data;

@Schema
@Data
public class AttributeUpdater {

    @Schema(
        title = "uniqueCode",
        description = "唯一编码"
    )
    private String uniqueCode;

    @Schema(
        title = "controlType",
        description = "前端控件类型"
    )
    private WebControlType controlType;

    private Long id;

    public void updateAttribute(Attribute param) {
        Optional.ofNullable(getUniqueCode()).ifPresent(param::setUniqueCode);
        Optional.ofNullable(getControlType()).ifPresent(param::setControlType);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
