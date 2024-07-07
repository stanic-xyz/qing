package cn.chenyunlong.qing.domain.auth.department.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class DepartmentUpdateRequest implements Request {

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

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
