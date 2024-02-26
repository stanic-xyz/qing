package cn.chenyunlong.qing.domain.productcenter.product.dto.response;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractJpaResponse;
import cn.chenyunlong.qing.domain.productcenter.product.ProductType;
import cn.chenyunlong.qing.domain.productcenter.product.SerializeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Schema
@EqualsAndHashCode(
    callSuper = true
)
public class ProductResponse extends AbstractJpaResponse {

    @Schema(
        title = "templateId",
        description = "模板Id"
    )
    private Long templateId;

    @Schema(
        title = "brandId",
        description = "品牌ID"
    )
    private Long brandId;

    @Schema(
        title = "code",
        description = "编码"
    )
    private String code;

    @Schema(
        title = "name",
        description = "名称"
    )
    private String name;

    @Schema(
        title = "categoryId",
        description = "分类ID"
    )
    private Long categoryId;

    @Schema(
        title = "description",
        description = "描述"
    )
    private String description;

    @Schema(
        title = "type",
        description = "产品类型"
    )
    private ProductType type;

    @Schema(
        title = "serializeType",
        description = "序列化类型"
    )
    private SerializeType serializeType;

    @Schema(
        title = "pid",
        description = "pid"
    )
    private Long pid;

    @Schema(
        title = "validStatus",
        description = "validStatus"
    )
    private ValidStatus validStatus;
}
