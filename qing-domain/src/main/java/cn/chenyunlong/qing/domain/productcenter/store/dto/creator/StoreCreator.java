package cn.chenyunlong.qing.domain.productcenter.store.dto.creator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class StoreCreator {

    @Schema(
        title = "name",
        description = "仓库名称"
    )
    private String name;

    @Schema(
        title = "description",
        description = "备注"
    )
    private String description;
}
