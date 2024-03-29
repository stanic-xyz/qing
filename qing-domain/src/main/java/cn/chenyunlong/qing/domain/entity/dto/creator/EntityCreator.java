package cn.chenyunlong.qing.domain.entity.dto.creator;

import cn.chenyunlong.qing.domain.entity.EntityType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class EntityCreator {

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public Long getZanCount() {
        return zanCount;
    }

    public void setZanCount(Long zanCount) {
        this.zanCount = zanCount;
    }
}
