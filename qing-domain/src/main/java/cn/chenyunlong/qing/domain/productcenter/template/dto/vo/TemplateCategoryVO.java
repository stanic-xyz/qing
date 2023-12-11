package cn.chenyunlong.qing.domain.productcenter.template.dto.vo;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.productcenter.template.TemplateCategory;
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
public class TemplateCategoryVO extends AbstractBaseJpaVo {
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

    public TemplateCategoryVO(TemplateCategory source) {
        super();
        this.setId(source.getId());
        ;
        this.setCreatedAt(source.getCreatedAt());
        ;
        this.setUpdatedAt(source.getCreatedAt());
        ;
        this.setName(source.getName());
        this.setPid(source.getPid());
        this.setSortNum(source.getSortNum());
        this.setValidStatus(source.getValidStatus());
    }
}
