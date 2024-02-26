package cn.chenyunlong.qing.domain.attribute.dto.request;

import cn.chenyunlong.common.model.Request;
import cn.chenyunlong.qing.domain.attribute.WebControlType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class AttributeUpdateRequest implements Request {

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

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
