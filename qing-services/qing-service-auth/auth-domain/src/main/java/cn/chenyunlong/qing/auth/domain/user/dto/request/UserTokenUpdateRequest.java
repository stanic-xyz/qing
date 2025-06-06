package cn.chenyunlong.qing.auth.domain.user.dto.request;

import cn.chenyunlong.common.model.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class UserTokenUpdateRequest implements Request {

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

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
