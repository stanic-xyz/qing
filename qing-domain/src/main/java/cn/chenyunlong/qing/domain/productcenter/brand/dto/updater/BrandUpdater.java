package cn.chenyunlong.qing.domain.productcenter.brand.dto.updater;

import cn.chenyunlong.qing.domain.productcenter.brand.Brand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Optional;

@Schema
@Data
public class BrandUpdater {
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

    @NotNull
    private Long id;

    public void updateBrand(Brand param) {
        Optional.ofNullable(getName()).ifPresent(param::setName);
        Optional.ofNullable(getLogo()).ifPresent(param::setLogo);
        Optional.ofNullable(getDescription()).ifPresent(param::setDescription);
        Optional.ofNullable(getProvider()).ifPresent(param::setProvider);
    }
}
