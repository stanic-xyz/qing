package cn.chenyunlong.qing.domain.zan.dto.vo;

import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.zan.Zan;
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
public class ZanVO extends AbstractBaseJpaVo {

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

    /**
     * 创建vo对象。
     */
    public ZanVO(Zan source) {
        super();
        this.setId(source.getId());
        this.setCreatedAt(source.getCreatedAt());
        this.setUpdatedAt(source.getCreatedAt());
        this.setUserId(source.getUserId());
        this.setEntityId(source.getEntityId());
    }

}
