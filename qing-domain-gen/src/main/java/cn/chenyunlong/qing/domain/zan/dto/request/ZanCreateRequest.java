package cn.chenyunlong.qing.domain.zan.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class ZanCreateRequest implements Request {

    @Schema(
        title = "userId",
        description = "userId"
    )
    private Long userId;

    @Schema(
        title = "entityId",
        description = "entityId"
    )
    private Long entityId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}
