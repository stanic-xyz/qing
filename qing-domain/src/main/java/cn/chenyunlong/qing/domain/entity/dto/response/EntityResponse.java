package cn.chenyunlong.qing.domain.entity.dto.response;

import cn.chenyunlong.common.model.AbstractJpaResponse;
import cn.chenyunlong.qing.domain.entity.EntityType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class EntityResponse extends AbstractJpaResponse {

    static {
    }

    @Schema(
        title = "name",
        description = "用户名"
    )
    private String name;

    @Schema(
        title = "entityType",
        description = "实体类型"
    )
    private EntityType entityType;

    @Schema(
        title = "zanCount",
        description = "点赞数量"
    )
    private Long zanCount;

}
