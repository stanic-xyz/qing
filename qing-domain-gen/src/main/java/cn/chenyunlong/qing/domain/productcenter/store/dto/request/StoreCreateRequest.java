package cn.chenyunlong.qing.domain.productcenter.store.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class StoreCreateRequest implements Request {

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
