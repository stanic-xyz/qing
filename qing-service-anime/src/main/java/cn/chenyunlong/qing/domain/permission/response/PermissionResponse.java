// ---Auto Generated by Project Qing ---
package cn.chenyunlong.qing.domain.permission.response;

import cn.chenyunlong.common.model.AbstractJpaResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class PermissionResponse extends AbstractJpaResponse {
    @Schema(
            title = "name"
    )
    private String name;

    @Schema(
            title = "description"
    )
    private String description;
}