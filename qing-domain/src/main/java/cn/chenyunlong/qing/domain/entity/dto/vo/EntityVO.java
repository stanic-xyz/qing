package cn.chenyunlong.qing.domain.entity.dto.vo;

import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.entity.Entity;
import cn.chenyunlong.qing.domain.entity.EntityType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Schema
@Data
@EqualsAndHashCode(
    callSuper = true
)
@NoArgsConstructor(
    access = AccessLevel.PROTECTED
)
public class EntityVO extends AbstractBaseJpaVo {
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

    public EntityVO(Entity source) {
        super();
        this.setId(source.getId());
        this.setCreatedAt(source.getCreatedAt());
        this.setUpdatedAt(source.getCreatedAt());
        this.setName(source.getName());
        this.setEntityType(source.getEntityType());
        this.setZanCount(source.getZanCount());
    }

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
