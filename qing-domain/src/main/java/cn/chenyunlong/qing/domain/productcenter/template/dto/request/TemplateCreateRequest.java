package cn.chenyunlong.qing.domain.productcenter.template.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class TemplateCreateRequest implements Request {

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
    private Integer templateType;

    @Schema(
        title = "metaData",
        description = "元数据"
    )
    private String metaData;
}
