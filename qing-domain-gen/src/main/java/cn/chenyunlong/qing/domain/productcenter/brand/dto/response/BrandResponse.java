package cn.chenyunlong.qing.domain.productcenter.brand.dto.response;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractJpaResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Schema
@EqualsAndHashCode(
    callSuper = true
)
public class BrandResponse extends AbstractJpaResponse {

    @Schema(
        title = "name",
        description = "品牌名称"
    )
    private String name;

    @Schema(
        title = "logo",
        description = "品牌logo"
    )
    private String logo;

    @Schema(
        title = "description",
        description = "品牌描述"
    )
    private String description;

    @Schema(
        title = "provider",
        description = "供应商"
    )
    private String provider;

    @Schema(
        title = "validStatus",
        description = "validStatus"
    )
    private ValidStatus validStatus;
}
