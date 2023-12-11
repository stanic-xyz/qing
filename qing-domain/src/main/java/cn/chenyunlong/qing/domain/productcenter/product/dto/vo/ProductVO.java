package cn.chenyunlong.qing.domain.productcenter.product.dto.vo;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.productcenter.product.Product;
import cn.chenyunlong.qing.domain.productcenter.product.ProductType;
import cn.chenyunlong.qing.domain.productcenter.product.SerializeType;
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
public class ProductVO extends AbstractBaseJpaVo {
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

    public ProductVO(Product source) {
        super();
        this.setId(source.getId());
        ;
        this.setCreatedAt(source.getCreatedAt());
        ;
        this.setUpdatedAt(source.getCreatedAt());
        ;
        this.setTemplateId(source.getTemplateId());
        this.setBrandId(source.getBrandId());
        this.setCode(source.getCode());
        this.setName(source.getName());
        this.setCategoryId(source.getCategoryId());
        this.setDescription(source.getDescription());
        this.setType(source.getType());
        this.setSerializeType(source.getSerializeType());
        this.setPid(source.getPid());
        this.setValidStatus(source.getValidStatus());
    }
}
