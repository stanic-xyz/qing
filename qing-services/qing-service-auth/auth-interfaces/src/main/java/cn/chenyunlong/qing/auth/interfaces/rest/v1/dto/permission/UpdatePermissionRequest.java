package cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.permission;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 更新权限请求
 */
@Data
public class UpdatePermissionRequest {

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String resource;

    @NotBlank
    private String action;

    private Integer sortOrder;
}

