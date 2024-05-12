package cn.chenyunlong.qing.domain.productcenter.templateitem.dto.updater;

import cn.chenyunlong.qing.domain.productcenter.templateitem.ComponentType;
import cn.chenyunlong.qing.domain.productcenter.templateitem.TemplateItem;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.Data;

@Schema
@Data
public class TemplateItemUpdater {

    @Schema(
        title = "name",
        description = "名称"
    )
    private String name;

    @Schema(
        title = "code",
        description = "编码"
    )
    private String code;

    @Schema(
        title = "defaultValue",
        description = "默认值"
    )
    private String defaultValue;

    @Schema(
        title = "placeholder",
        description = "占位符"
    )
    private String placeholder;

    @Schema(
        title = "createUser",
        description = "创建人"
    )
    private String createUser;

    @Schema(
        title = "sortNum",
        description = "排序号"
    )
    private BigDecimal sortNum;

    @Schema(
        title = "dataUrl",
        description = "数据的请求url"
    )
    private String dataUrl;

    @Schema(
        title = "componentType",
        description = "组件类型"
    )
    private ComponentType componentType;

    @Schema(
        title = "description",
        description = "描述"
    )
    private String description;

    @Schema(
        title = "metaData",
        description = "元数据"
    )
    private String metaData;

    private Long id;

    public void updateTemplateItem(TemplateItem param) {
        Optional.ofNullable(getName()).ifPresent(param::setName);
        Optional.ofNullable(getCode()).ifPresent(param::setCode);
        Optional.ofNullable(getDefaultValue()).ifPresent(param::setDefaultValue);
        Optional.ofNullable(getPlaceholder()).ifPresent(param::setPlaceholder);
        Optional.ofNullable(getCreateUser()).ifPresent(param::setCreateUser);
        Optional.ofNullable(getSortNum()).ifPresent(param::setSortNum);
        Optional.ofNullable(getDataUrl()).ifPresent(param::setDataUrl);
        Optional.ofNullable(getComponentType()).ifPresent(param::setComponentType);
        Optional.ofNullable(getDescription()).ifPresent(param::setDescription);
        Optional.ofNullable(getMetaData()).ifPresent(param::setMetaData);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
