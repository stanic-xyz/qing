package cn.chenyunlong.qing.domain.auth.department.dto.vo;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.auth.department.Department;
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
public class DepartmentVO extends AbstractBaseJpaVo {

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

    public DepartmentVO(Department source) {
        super();
        this.setId(source.getId());
        this.setCreatedAt(source.getCreatedAt());
        this.setUpdatedAt(source.getCreatedAt());
        this.setVersion(source.getVersion());
        this.setName(source.getName());
        this.setPid(source.getPid());
        this.setSortNum(source.getSortNum());
        this.setValidStatus(source.getValidStatus());
    }
}
