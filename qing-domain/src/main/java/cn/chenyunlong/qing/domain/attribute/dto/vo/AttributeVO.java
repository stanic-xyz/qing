package cn.chenyunlong.qing.domain.attribute.dto.vo;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.attribute.Attribute;
import cn.chenyunlong.qing.domain.attribute.WebControlType;
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
public class AttributeVO extends AbstractBaseJpaVo {
    @Schema(
        title = "uniqueCode",
        description = "唯一编码"
    )
    private String uniqueCode;

    @Schema(
        title = "controlType",
        description = "前端控件类型"
    )
    private WebControlType controlType;

    @Schema(
        title = "validStatus",
        description = "validStatus"
    )
    private ValidStatus validStatus;

    public AttributeVO(Attribute source) {
        super();
        this.setId(source.getId());
        ;
        this.setCreatedAt(source.getCreatedAt());
        ;
        this.setUpdatedAt(source.getCreatedAt());
        ;
        this.setUniqueCode(source.getUniqueCode());
        this.setControlType(source.getControlType());
        this.setValidStatus(source.getValidStatus());
    }
}
