package cn.chenyunlong.qing.domain.zan.dto.updater;

import cn.chenyunlong.qing.domain.zan.Zan;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Optional;

@Schema
@Data
public class ZanUpdater {

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

    public void updateZan(Zan param) {
        Optional.ofNullable(getUserId()).ifPresent(param::setUserId);
        Optional.ofNullable(getEntityId()).ifPresent(param::setEntityId);
    }

}
