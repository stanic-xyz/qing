package cn.chenyunlong.qing.domain.auth.role.dto.vo;

import cn.chenyunlong.common.constants.ValidStatus;
import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.auth.role.Role;
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
public class RoleVO extends AbstractBaseJpaVo {

    @Schema(
        title = "role",
        description = "角色编码"
    )
    private String role;

    @Schema(
        title = "name",
        description = "角色名称"
    )
    private String name;

    @Schema(
        title = "platformId",
        description = "平台Id"
    )
    private Long platformId;

    @Schema(
        title = "remark",
        description = "备注"
    )
    private String remark;

    @Schema(
        title = "validStatus",
        description = "validStatus"
    )
    private ValidStatus validStatus;

    public RoleVO(Role source) {
        super();
        this.setId(source.getId());
        this.setCreatedAt(source.getCreatedAt());
        this.setUpdatedAt(source.getCreatedAt());
        this.setVersion(source.getVersion());
        this.setRole(source.getRole());
        this.setName(source.getName());
        this.setPlatformId(source.getPlatformId());
        this.setRemark(source.getRemark());
        this.setValidStatus(source.getValidStatus());
    }
}
