package cn.chenyunlong.qing.domain.productcenter.store.dto.response;

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
public class StoreResponse extends AbstractJpaResponse {

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

    @Schema(
        title = "validStatus",
        description = "validStatus"
    )
    private ValidStatus validStatus;
}
