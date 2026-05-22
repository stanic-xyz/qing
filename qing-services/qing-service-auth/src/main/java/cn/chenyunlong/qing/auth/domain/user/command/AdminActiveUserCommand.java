package cn.chenyunlong.qing.auth.domain.user.command;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminActiveUserCommand {

    private Long userId;

    private String adminUsername;

    public static AdminActiveUserCommand create(Long userId, String adminUsername) {
        return new AdminActiveUserCommand(userId, adminUsername);
    }
}
