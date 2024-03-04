package cn.chenyunlong.qing.domain.auth.user.dto.vo;

import cn.chenyunlong.common.enums.MFAType;
import cn.chenyunlong.common.model.AbstractBaseJpaVo;
import cn.chenyunlong.qing.domain.auth.user.QingUser;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
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
public class QingUserVO extends AbstractBaseJpaVo {

    @Schema(title = "uid", description = "用户唯一ID")
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

    @Schema(
        title = "password",
        description = "password"
    )
    private String password;

    @Schema(
        title = "phone",
        description = "phone"
    )
    private String phone;

    @Schema(
        title = "email",
        description = "email"
    )
    private String email;

    @Schema(
        title = "avatar",
        description = "avatar"
    )
    private String avatar;

    @Schema(
        title = "description",
        description = "description"
    )
    private String description;

    @Schema(
        title = "expireTime",
        description = "expireTime"
    )
    private LocalDateTime expireTime;

    @Schema(
        title = "mfaType",
        description = "mfaType"
    )
    private MFAType mfaType;

    @Schema(
        title = "mfaKey",
        description = "mfaKey"
    )
    private String mfaKey;

    public QingUserVO(QingUser source) {
        super();
        this.setId(source.getId());
        this.setCreatedAt(source.getCreatedAt());
        this.setUpdatedAt(source.getCreatedAt());
        this.setVersion(source.getVersion());
        this.setUid(source.getUid());
        this.setUsername(source.getUsername());
        this.setNickname(source.getNickname());
        this.setPassword(source.getPassword());
        this.setPhone(source.getPhone());
        this.setEmail(source.getEmail());
        this.setAvatar(source.getAvatar());
        this.setDescription(source.getDescription());
        this.setExpireTime(source.getExpireTime());
        this.setMfaType(source.getMfaType());
        this.setMfaKey(source.getMfaKey());
    }
}
