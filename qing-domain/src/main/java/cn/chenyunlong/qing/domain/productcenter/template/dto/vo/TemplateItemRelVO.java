package cn.chenyunlong.qing.domain.productcenter.template.dto.vo;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.productcenter.template.TemplateItemRel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Schema
@Data
@EqualsAndHashCode(
        callSuper = true
)
@NoArgsConstructor(
        access = AccessLevel.PROTECTED
)
public class TemplateItemRelVO extends AbstractBaseJpaVo {
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

    public TemplateItemRelVO(TemplateItemRel source) {
        super();
        this.setId(source.getId());
        ;
        this.setCreatedAt(source.getCreatedAt());
        ;
        this.setUpdatedAt(source.getCreatedAt());
        ;
        this.setTemplateId(source.getTemplateId());
        this.setTemplateItemId(source.getTemplateItemId());
        this.setRuleJson(source.getRuleJson());
        this.setValidStatus(source.getValidStatus());
    }
}
