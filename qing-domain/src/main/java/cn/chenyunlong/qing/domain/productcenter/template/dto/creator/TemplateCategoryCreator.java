package cn.chenyunlong.qing.domain.productcenter.template.dto.creator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class TemplateCategoryCreator {
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
}
