package cn.chenyunlong.qing.domain.auth.role.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class RoleUpdateRequest implements Request {
    @Schema(
            title = "role",
            description = "角色编码"
    )
    private String role;

    @Schema(
            title = "name",
            description = "角色名称"
    )
    private String name;

    @Schema(
            title = "platformId",
            description = "平台Id"
    )
    private Long platformId;

    @Schema(
            title = "remark",
            description = "备注"
    )
    private String remark;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
