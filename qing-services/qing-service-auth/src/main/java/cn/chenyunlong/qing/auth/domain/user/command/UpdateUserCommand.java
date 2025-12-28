package cn.chenyunlong.qing.auth.domain.user.command;

import cn.chenyunlong.qing.auth.domain.user.valueObject.UserId;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserCommand {

    private UserId userId;

    private String avatar;
}
