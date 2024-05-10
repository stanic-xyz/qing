package cn.chenyunlong.qing.domain.productcenter.template.dto.creator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class TemplateItemRelCreator {

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
}
