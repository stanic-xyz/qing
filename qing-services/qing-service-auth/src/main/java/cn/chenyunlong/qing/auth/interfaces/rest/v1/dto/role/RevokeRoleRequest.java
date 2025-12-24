package cn.chenyunlong.qing.auth.interfaces.rest.v1.dto.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevokeRoleRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private Long roleId;
}
