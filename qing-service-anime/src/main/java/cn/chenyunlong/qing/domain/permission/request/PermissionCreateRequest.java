// ---Auto Generated by Project Qing ---
package cn.chenyunlong.qing.domain.permission.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class PermissionCreateRequest implements Request {
    @Schema(
            title = "name"
    )
    private String name;

    @Schema(
            title = "description"
    )
    private String description;
}