package cn.chenyunlong.qing.domain.productcenter.template.dto.response;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractJpaResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Schema
@EqualsAndHashCode(
    callSuper = true
)
public class TemplateItemRelResponse extends AbstractJpaResponse {

    @Schema(
        title = "templateId",
        description = "模板ID"
    )
    private Long templateId;

    @Schema(
        title = "templateItemId",
        description = "模板项ID"
    )
    private Long templateItemId;

    @Schema(
        title = "ruleJson",
        description = "校验的结构化list"
    )
    private String ruleJson;

    @Schema(
        title = "validStatus",
        description = "validStatus"
    )
    private ValidStatus validStatus;
}
