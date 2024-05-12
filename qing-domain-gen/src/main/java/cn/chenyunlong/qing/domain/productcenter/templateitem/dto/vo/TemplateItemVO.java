package cn.chenyunlong.qing.domain.productcenter.templateitem.dto.vo;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.productcenter.templateitem.ComponentType;
import cn.chenyunlong.qing.domain.productcenter.templateitem.TemplateItem;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
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
public class TemplateItemVO extends AbstractBaseJpaVo {

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

    @Schema(
        title = "validStatus",
        description = "validStatus"
    )
    private ValidStatus validStatus;

    public TemplateItemVO(TemplateItem source) {
        super();
        this.setId(source.getId());
        this.setCreatedAt(source.getCreatedAt());
        this.setUpdatedAt(source.getCreatedAt());
        this.setName(source.getName());
        this.setCode(source.getCode());
        this.setDefaultValue(source.getDefaultValue());
        this.setPlaceholder(source.getPlaceholder());
        this.setCreateUser(source.getCreateUser());
        this.setSortNum(source.getSortNum());
        this.setDataUrl(source.getDataUrl());
        this.setComponentType(source.getComponentType());
        this.setDescription(source.getDescription());
        this.setMetaData(source.getMetaData());
        this.setValidStatus(source.getValidStatus());
    }
}
