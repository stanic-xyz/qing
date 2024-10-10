package cn.chenyunlong.qing.domain.auth.department.dto.updater;

import cn.chenyunlong.qing.domain.auth.department.Department;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Optional;

@Schema
@Data
public class DepartmentUpdater {

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

    private Long id;

    public void updateDepartment(Department param) {
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
