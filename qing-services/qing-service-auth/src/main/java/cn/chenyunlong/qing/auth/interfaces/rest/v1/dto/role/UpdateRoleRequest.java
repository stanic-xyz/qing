package cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 更新角色请求DTO
 */
@Data
public class UpdateRoleRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;
}
