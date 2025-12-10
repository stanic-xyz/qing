package cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.permission;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 创建权限请求
 */
@Data
public class CreatePermissionRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String type;

    @NotBlank
    private String resource;

    @NotBlank
    private String action;

    private Integer sortOrder;

    private Long parentId;
}

