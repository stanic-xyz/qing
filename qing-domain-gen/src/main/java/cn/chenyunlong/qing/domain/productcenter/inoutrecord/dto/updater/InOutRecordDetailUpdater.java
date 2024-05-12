package cn.chenyunlong.qing.domain.productcenter.inoutrecord.dto.updater;

import cn.chenyunlong.qing.domain.productcenter.inoutrecord.InOutRecordDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import lombok.Data;

@Schema
@Data
public class InOutRecordDetailUpdater {

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

    private Long id;

    public void updateInOutRecordDetail(InOutRecordDetail param) {
        Optional.ofNullable(getUniqueCodes()).ifPresent(param::setUniqueCodes);
        Optional.ofNullable(getExtInfo()).ifPresent(param::setExtInfo);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
