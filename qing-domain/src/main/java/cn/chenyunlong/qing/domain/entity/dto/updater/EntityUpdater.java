package cn.chenyunlong.qing.domain.entity.dto.updater;

import cn.chenyunlong.qing.domain.entity.Entity;
import cn.chenyunlong.qing.domain.entity.EntityType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import lombok.Data;

@Schema
@Data
public class EntityUpdater {

    private Long id;

    @Schema(title = "name", description = "用户名")
    private String name;

    @Schema(title = "entityType", description = "实体类型")
    private EntityType entityType;

    @Schema(title = "zanCount", description = "点赞数量")
    private Long zanCount;

    public void updateEntity(Entity param) {
        Optional.ofNullable(getName()).ifPresent(param::setName);
        Optional.ofNullable(getEntityType()).ifPresent(param::setEntityType);
        Optional.ofNullable(getZanCount()).ifPresent(param::setZanCount);
    }
}
