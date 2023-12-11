package cn.chenyunlong.qing.domain.productcenter.template.dto.response;

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
public class TemplateCategoryResponse extends AbstractJpaResponse {
    @Schema(
            title = "name",
            description = "分类名称"
    )
    private String name;

    @Schema(
            title = "pid",
            description = "父节点ID"
    )
    private Long pid;

    @Schema(
            title = "sortNum",
            description = "排序号"
    )
    private Integer sortNum;

    @Schema(
            title = "validStatus",
            description = "validStatus"
    )
    private ValidStatus validStatus;
}
