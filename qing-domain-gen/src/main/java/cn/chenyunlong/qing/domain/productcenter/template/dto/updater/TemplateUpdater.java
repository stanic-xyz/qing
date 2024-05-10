package cn.chenyunlong.qing.domain.productcenter.template.dto.updater;

import cn.chenyunlong.qing.domain.productcenter.template.Template;
import cn.chenyunlong.qing.domain.productcenter.template.TemplateType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import lombok.Data;

@Schema
@Data
public class TemplateUpdater {

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

    private Long id;

    public void updateTemplate(Template param) {
        Optional.ofNullable(getName()).ifPresent(param::setName);
        Optional.ofNullable(getDescription()).ifPresent(param::setDescription);
        Optional.ofNullable(getCode()).ifPresent(param::setCode);
        Optional.ofNullable(getCategoryId()).ifPresent(param::setCategoryId);
        Optional.ofNullable(getTemplateType()).ifPresent(param::setTemplateType);
        Optional.ofNullable(getMetaData()).ifPresent(param::setMetaData);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
