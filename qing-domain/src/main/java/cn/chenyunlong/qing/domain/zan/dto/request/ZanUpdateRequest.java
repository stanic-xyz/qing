package cn.chenyunlong.qing.domain.zan.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class ZanUpdateRequest implements Request {
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

    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
