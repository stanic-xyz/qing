package cn.chenyunlong.qing.domain.productcenter.template.dto.vo;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.productcenter.template.Template;
import cn.chenyunlong.qing.domain.productcenter.template.TemplateType;
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
public class TemplateVO extends AbstractBaseJpaVo {

    @Schema(
        title = "name",
        description = "名称"
    )
    private String name;

    @Schema(
        title = "description",
        description = "描述"
    )
    private String description;

    @Schema(
        title = "code",
        description = "编码"
    )
    private String code;

    @Schema(
        title = "categoryId",
        description = "分类ID"
    )
    private Long categoryId;

    @Schema(
        title = "templateType",
        description = "templateType"
    )
    private TemplateType templateType;

    @Schema(
        title = "metaData",
        description = "元数据"
    )
    private String metaData;

    @Schema(
        title = "validStatus",
        description = "validStatus"
    )
    private ValidStatus validStatus;

    public TemplateVO(Template source) {
        super();
        this.setId(source.getId());
        this.setCreatedAt(source.getCreatedAt());
        this.setUpdatedAt(source.getCreatedAt());
        this.setName(source.getName());
        this.setDescription(source.getDescription());
        this.setCode(source.getCode());
        this.setCategoryId(source.getCategoryId());
        this.setTemplateType(source.getTemplateType());
        this.setMetaData(source.getMetaData());
        this.setValidStatus(source.getValidStatus());
    }
}
