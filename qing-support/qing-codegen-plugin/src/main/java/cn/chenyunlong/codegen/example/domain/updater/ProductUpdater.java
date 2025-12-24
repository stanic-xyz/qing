package cn.chenyunlong.codegen.example.domain.updater;

import cn.chenyunlong.codegen.example.domain.Product;
import cn.chenyunlong.qing.domain.common.AggregateId;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.Data;

@Schema
@Data
public class ProductUpdater {
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
            title = "sortOrder",
            description = "sortOrder"
    )
    private Integer sortOrder;

    @Schema(
            title = "remarks",
            description = "remarks"
    )
    private String remarks;

    private AggregateId id;

    public void updateProduct(Product param) {
        Optional.ofNullable(getName()).ifPresent(param::setName);
        Optional.ofNullable(getCode()).ifPresent(param::setCode);
        Optional.ofNullable(getDescription()).ifPresent(param::setDescription);
        Optional.ofNullable(getPrice()).ifPresent(param::setPrice);
        Optional.ofNullable(getStockQuantity()).ifPresent(param::setStockQuantity);
        Optional.ofNullable(getStatus()).ifPresent(param::setStatus);
        Optional.ofNullable(getCategoryId()).ifPresent(param::setCategoryId);
        Optional.ofNullable(getBrandId()).ifPresent(param::setBrandId);
        Optional.ofNullable(getWeight()).ifPresent(param::setWeight);
        Optional.ofNullable(getDimensions()).ifPresent(param::setDimensions);
        Optional.ofNullable(getIsRecommended()).ifPresent(param::setIsRecommended);
        Optional.ofNullable(getSaleStartTime()).ifPresent(param::setSaleStartTime);
        Optional.ofNullable(getSaleEndTime()).ifPresent(param::setSaleEndTime);
        Optional.ofNullable(getSortOrder()).ifPresent(param::setSortOrder);
        Optional.ofNullable(getRemarks()).ifPresent(param::setRemarks);
    }

    public AggregateId getId() {
        return id;
    }

    public void setId(AggregateId id) {
        this.id = id;
    }
}
