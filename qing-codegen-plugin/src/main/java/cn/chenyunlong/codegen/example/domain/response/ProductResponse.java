package cn.chenyunlong.codegen.example.domain.response;

import cn.chenyunlong.codegen.example.domain.Product;
import cn.chenyunlong.common.model.AbstractJpaResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Schema
@EqualsAndHashCode(
        callSuper = true
)
public class ProductResponse extends AbstractJpaResponse {
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
}
