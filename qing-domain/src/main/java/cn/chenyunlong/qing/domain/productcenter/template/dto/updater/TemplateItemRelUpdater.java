package cn.chenyunlong.qing.domain.productcenter.template.dto.updater;

import cn.chenyunlong.qing.domain.productcenter.template.TemplateItemRel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Optional;

@Schema
@Data
public class TemplateItemRelUpdater {
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

    private Long id;

    public void updateTemplateItemRel(TemplateItemRel param) {
        Optional.ofNullable(getTemplateId()).ifPresent(param::setTemplateId);
        Optional.ofNullable(getTemplateItemId()).ifPresent(param::setTemplateItemId);
        Optional.ofNullable(getRuleJson()).ifPresent(param::setRuleJson);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
