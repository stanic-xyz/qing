package cn.chenyunlong.qing.domain.zan.dto.response;

import cn.chenyunlong.common.model.AbstractJpaResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class ZanResponse extends AbstractJpaResponse {
    static {
    }

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
