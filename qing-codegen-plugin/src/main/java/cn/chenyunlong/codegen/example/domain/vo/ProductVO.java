package cn.chenyunlong.codegen.example.domain.vo;

import cn.chenyunlong.codegen.example.domain.Product;
import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
            title = "name",
            description = "name"
    )
    private String name;

    @Schema(
            title = "code",
            description = "code"
    )
    private String code;

    @Schema(
            title = "description",
            description = "description"
    )
    private String description;

    @Schema(
            title = "price",
            description = "price"
    )
    private BigDecimal price;

    @Schema(
            title = "stockQuantity",
            description = "stockQuantity"
    )
    private Integer stockQuantity;

    @Schema(
            title = "status",
            description = "status"
    )
    private Product.ProductStatus status;

    @Schema(
            title = "categoryId",
            description = "categoryId"
    )
    private Long categoryId;

    @Schema(
            title = "brandId",
            description = "brandId"
    )
    private Long brandId;

    @Schema(
            title = "weight",
            description = "weight"
    )
    private Integer weight;

    @Schema(
            title = "dimensions",
            description = "dimensions"
    )
    private String dimensions;

    @Schema(
            title = "isRecommended",
            description = "isRecommended"
    )
    private Boolean isRecommended;

    @Schema(
            title = "saleStartTime",
            description = "saleStartTime"
    )
    private LocalDateTime saleStartTime;

    @Schema(
            title = "saleEndTime",
            description = "saleEndTime"
    )
    private LocalDateTime saleEndTime;

    @Schema(
            title = "tags",
            description = "tags"
    )
    private List<String> tags;

    @Schema(
            title = "imageUrls",
            description = "imageUrls"
    )
    private List<String> imageUrls;

    @Schema(
            title = "sortOrder",
            description = "sortOrder"
    )
    private Integer sortOrder;

    @Schema(
            title = "remarks",
            description = "remarks"
    )
    private String remarks;

    public ProductVO(Product source) {
        super();
        this.setId(source.getId().getId());;
        this.setCreatedAt(source.getCreatedAt());;
        this.setUpdatedAt(source.getCreatedAt());;
        this.setVersion(source.getVersion());;
        this.setName(source.getName());
        this.setCode(source.getCode());
        this.setDescription(source.getDescription());
        this.setPrice(source.getPrice());
        this.setStockQuantity(source.getStockQuantity());
        this.setStatus(source.getStatus());
        this.setCategoryId(source.getCategoryId());
        this.setBrandId(source.getBrandId());
        this.setWeight(source.getWeight());
        this.setDimensions(source.getDimensions());
        this.setIsRecommended(source.getIsRecommended());
        this.setSaleStartTime(source.getSaleStartTime());
        this.setSaleEndTime(source.getSaleEndTime());
        this.setTags(source.getTags());
        this.setImageUrls(source.getImageUrls());
        this.setSortOrder(source.getSortOrder());
        this.setRemarks(source.getRemarks());
    }
}
