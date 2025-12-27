package cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.permission;

import cn.chenyunlong.qing.auth.domain.rbac.PermissionStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * 更新权限请求
 */
@Data
public class UpdatePermissionStatusRequest {

    private PermissionStatus status;
}
