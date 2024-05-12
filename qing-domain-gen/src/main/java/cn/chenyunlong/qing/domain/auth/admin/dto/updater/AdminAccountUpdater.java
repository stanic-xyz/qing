package cn.chenyunlong.qing.domain.auth.admin.dto.updater;

import cn.chenyunlong.qing.domain.auth.admin.AdminAccount;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import lombok.Data;

@Schema
@Data
public class AdminAccountUpdater {

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

    private Long id;

    public void updateAdminAccount(AdminAccount param) {
        Optional.ofNullable(getPhone()).ifPresent(param::setPhone);
        Optional.ofNullable(getPassword()).ifPresent(param::setPassword);
        Optional.ofNullable(getUsername()).ifPresent(param::setUsername);
        Optional.ofNullable(getUid()).ifPresent(param::setUid);
        Optional.ofNullable(getRealName()).ifPresent(param::setRealName);
        Optional.ofNullable(getDepartmentId()).ifPresent(param::setDepartmentId);
        Optional.ofNullable(getExtInfo()).ifPresent(param::setExtInfo);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
