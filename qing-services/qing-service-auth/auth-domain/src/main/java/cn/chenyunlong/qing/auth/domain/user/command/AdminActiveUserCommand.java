package cn.chenyunlong.qing.auth.domain.user.command;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminActiveUserCommand {

    private Long uid;

    private String adminUsername;

    public static AdminActiveUserCommand create(Long uid, String adminUsername) {
        return new AdminActiveUserCommand(uid, adminUsername);
    }
}
