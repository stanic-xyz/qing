package cn.chenyunlong.qing.domain.productcenter.templateitem.dto.response;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractJpaResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.type.ComponentType;

import java.math.BigDecimal;

@Data
@Schema
@EqualsAndHashCode(
        callSuper = true
)
public class TemplateItemResponse extends AbstractJpaResponse {
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
}
