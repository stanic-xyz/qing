package cn.chenyunlong.qing.auth.domain.admin.dto.vo;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractBaseJpaVo;
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
public class AdminAccountVO extends AbstractBaseJpaVo {

    @Schema(
        title = "phone",
        description = "phone"
    )
    private String phone;

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

    @Schema(
        title = "validStatus",
        description = "validStatus"
    )
    private ValidStatus validStatus;

}
