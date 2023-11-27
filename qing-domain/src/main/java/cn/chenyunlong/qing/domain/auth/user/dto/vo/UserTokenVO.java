package cn.chenyunlong.qing.domain.auth.user.dto.vo;

import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.auth.user.UserToken;
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
public class UserTokenVO extends AbstractBaseJpaVo {
    @Schema(
            title = "uid",
            description = "用户唯一ID"
    )
    private Long uid;

    @Schema(
            title = "username",
            description = "用户名"
    )
    private String username;

    @Schema(
            title = "nickname",
            description = "昵称"
    )
    private String nickname;

    public UserTokenVO(UserToken source) {
        super();
        this.setId(source.getId());
        ;
        this.setCreatedAt(source.getCreatedAt());
        ;
        this.setUpdatedAt(source.getCreatedAt());
        ;
        this.setUid(source.getUid());
    }
}
