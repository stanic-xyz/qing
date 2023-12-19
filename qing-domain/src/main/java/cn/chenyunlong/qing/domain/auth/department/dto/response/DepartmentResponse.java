package cn.chenyunlong.qing.domain.auth.department.dto.response;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractJpaResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Schema
@EqualsAndHashCode(
        callSuper = true
)
public class DepartmentResponse extends AbstractJpaResponse {
    @Schema(
            title = "name",
            description = "部门名称"
    )
    private String name;

    @Schema(
            title = "pid",
            description = "上级单位"
    )
    private Long pid;

    @Schema(
            title = "sortNum",
            description = "sortNum"
    )
    private Integer sortNum;

    @Schema(
            title = "validStatus",
            description = "validStatus"
    )
    private ValidStatus validStatus;
}
