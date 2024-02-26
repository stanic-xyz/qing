package cn.chenyunlong.qing.domain.auth.admin.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class AdminAccountCreateRequest implements Request {

    @Schema(
        title = "phone",
        description = "phone"
    )
    private String phone;

    @Schema(
        title = "password",
        description = "password"
    )
    private String password;

    @Schema(
        title = "username",
        description = "username"
    )
    private String username;

    @Schema(
        title = "uid",
        description = "uid"
    )
    private String uid;

    @Schema(
        title = "realName",
        description = "realName"
    )
    private String realName;

    @Schema(
        title = "departmentId",
        description = "departmentId"
    )
    private Long departmentId;

    @Schema(
        title = "extInfo",
        description = "extInfo"
    )
    private String extInfo;
}
