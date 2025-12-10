package cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.permission;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 批量创建权限请求
 */
@Data
public class BatchCreatePermissionRequest {

    @NotEmpty
    @Valid
    private List<CreatePermissionRequest> items;
}

