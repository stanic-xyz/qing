package cn.chenyunlong.qing.auth.domain.user.command;

import cn.chenyunlong.qing.auth.domain.user.valueObject.Username;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserActiveCommand {

    private Username username;

    private String activeCode;

    public static UserActiveCommand create(String username, String activeCode) {
        return new UserActiveCommand(Username.of(username), activeCode);
    }
}
