package cn.chenyunlong.qing.auth.domain.user.command;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminDeActiveUserCommand {

    private Long userId;

    private String adminUsername;

    public static AdminDeActiveUserCommand create(Long userId, String adminUsername) {
        return new AdminDeActiveUserCommand(userId, adminUsername);
    }
}
