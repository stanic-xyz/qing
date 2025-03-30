package cn.chenyunlong.qing.auth.domain.role.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class RoleQuery {

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
}
