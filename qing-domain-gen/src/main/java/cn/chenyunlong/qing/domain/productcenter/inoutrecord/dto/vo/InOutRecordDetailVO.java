package cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.vo;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.productcenter.inoutrecord.InOutRecordDetail;
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
public class InOutRecordDetailVO extends AbstractBaseJpaVo {

    @Schema(
        title = "uniqueCodes",
        description = "唯一编码字符串，约定用;分隔"
    )
    private String uniqueCodes;

    @Schema(
        title = "extInfo",
        description = "扩展信息"
    )
    private String extInfo;

    @Schema(
        title = "validStatus",
        description = "validStatus"
    )
    private ValidStatus validStatus;

    public InOutRecordDetailVO(InOutRecordDetail source) {
        super();
        this.setId(source.getId());
        this.setCreatedAt(source.getCreatedAt());
        this.setUpdatedAt(source.getCreatedAt());
        this.setUniqueCodes(source.getUniqueCodes());
        this.setExtInfo(source.getExtInfo());
        this.setValidStatus(source.getValidStatus());
    }
}
