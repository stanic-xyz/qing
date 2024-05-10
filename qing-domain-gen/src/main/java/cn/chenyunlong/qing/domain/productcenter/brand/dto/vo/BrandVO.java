package cn.chenyunlong.qing.domain.productcenter.brand.dto.vo;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.productcenter.brand.Brand;
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
public class BrandVO extends AbstractBaseJpaVo {

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

    public BrandVO(Brand source) {
        super();
        this.setId(source.getId());
        this.setCreatedAt(source.getCreatedAt());
        this.setUpdatedAt(source.getCreatedAt());
        this.setName(source.getName());
        this.setLogo(source.getLogo());
        this.setDescription(source.getDescription());
        this.setProvider(source.getProvider());
        this.setValidStatus(source.getValidStatus());
    }
}
