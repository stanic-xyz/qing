package cn.chenyunlong.qing.domain.productcenter.store.dto.vo;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.productcenter.store.Store;
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
public class StoreVO extends AbstractBaseJpaVo {

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

    public StoreVO(Store source) {
        super();
        this.setId(source.getId());
        this.setCreatedAt(source.getCreatedAt());
        this.setUpdatedAt(source.getCreatedAt());
        this.setName(source.getName());
        this.setDescription(source.getDescription());
        this.setValidStatus(source.getValidStatus());
    }
}
