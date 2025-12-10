package cn.chenyunlong.qing.auth.domain.user.specification;

import cn.chenyunlong.qing.auth.domain.user.command.UserRegistrationCommand;
import cn.chenyunlong.qing.auth.domain.user.exception.UserRegistrationException;
import cn.chenyunlong.qing.auth.domain.user.repository.UserRepository;
import cn.chenyunlong.qing.auth.domain.user.valueObject.Email;
import cn.chenyunlong.qing.auth.domain.user.valueObject.Username;
import org.springframework.stereotype.Service;

/**
 * 用户注册规约
 */
@Service
public class UserRegistrationSpecification {

    private final UserRepository userRepository;

    public UserRegistrationSpecification(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isSatisfiedBy(UserRegistrationCommand command) {
        return isUsernameAvailable(command.username()) && isEmailAvailable(command.email());
    }

    public void check(UserRegistrationCommand command) {
        if (!isUsernameAvailable(command.username())) {
            throw new UserRegistrationException("用户名已存在");
        }
        if (!isEmailAvailable(command.email())) {
            throw new UserRegistrationException("邮箱已存在");
        }
        if (!isNicknameAvailable(command.nickname())) {
            throw new UserRegistrationException("昵称已存在");
        }
    }

    private boolean isUsernameAvailable(Username username) {
        return !userRepository.existsByUsername(username);
    }

    private boolean isEmailAvailable(Email email) {
        return !userRepository.existsByEmail(email);
    }

    private boolean isNicknameAvailable(String nickname) {
        return !userRepository.existsByNicknames(nickname);
    }
}
