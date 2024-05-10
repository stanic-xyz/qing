package cn.chenyunlong.qing.domain.productcenter.brand.dto.creator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class BrandCreator {

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
}
