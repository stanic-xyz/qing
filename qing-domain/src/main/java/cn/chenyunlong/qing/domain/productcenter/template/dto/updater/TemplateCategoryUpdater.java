package cn.chenyunlong.qing.domain.productcenter.template.dto.updater;

import cn.chenyunlong.qing.domain.productcenter.template.TemplateCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import lombok.Data;

@Schema
@Data
public class TemplateCategoryUpdater {

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

    public void updateTemplateCategory(TemplateCategory param) {
        Optional.ofNullable(getName()).ifPresent(param::setName);
        Optional.ofNullable(getPid()).ifPresent(param::setPid);
        Optional.ofNullable(getSortNum()).ifPresent(param::setSortNum);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
