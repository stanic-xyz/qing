package cn.chenyunlong.qing.domain.productcenter.template.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class TemplateCategoryUpdateRequest implements Request {
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

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
