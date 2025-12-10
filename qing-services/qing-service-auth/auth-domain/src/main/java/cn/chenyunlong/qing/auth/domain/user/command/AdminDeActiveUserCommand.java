package cn.chenyunlong.qing.auth.domain.user.command;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminDeActiveUserCommand {

    private Long uid;

    private String adminUsername;

    public static AdminDeActiveUserCommand create(Long uid, String adminUsername) {
        return new AdminDeActiveUserCommand(uid, adminUsername);
    }
}
