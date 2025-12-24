package cn.chenyunlong.qing.auth.domain.authentication.service;

import cn.chenyunlong.qing.auth.domain.authentication.exception.AuthenticationException;
import cn.chenyunlong.qing.auth.domain.authentication.valueObject.UserLockReason;
import cn.chenyunlong.qing.auth.domain.event.DomainEventPublisher;
import cn.chenyunlong.qing.auth.domain.user.User;
import cn.chenyunlong.qing.auth.domain.user.UserConnection;
import cn.chenyunlong.qing.auth.domain.user.command.*;
import cn.chenyunlong.qing.auth.domain.user.dto.entity.QingAuthUser;
import cn.chenyunlong.qing.auth.domain.user.event.EmailChangedEvent;
import cn.chenyunlong.qing.auth.domain.user.repository.UserRepository;
import cn.chenyunlong.qing.auth.domain.user.specification.UserAuthenticationSpecification;
import cn.chenyunlong.qing.auth.domain.user.specification.UserRegistrationSpecification;
import cn.chenyunlong.qing.auth.domain.user.valueObject.*;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class UserDomainService {

    private final UserRepository userRepository;
    private final UserRegistrationSpecification userRegistrationSpecification;
    private final UserAuthenticationSpecification authenticationSpecification;
    private final UserRepository repository;
    private final DomainEventPublisher eventPublisher;

    /**
     * createImpl
     */
    public User register(UserRegistrationCommand command) {
        userRegistrationSpecification.check(command);

        User user = User.register(
                UserId.generate(),
                command.username(),
                RawPassword.of(command.password()),
                command.phoneNumber(),
                command.email(),
                command.nickname());

        User save = repository.save(user);
        eventPublisher.publishAll(user.domainEvents());
        user.clearDomainEvents();
        return save;
    }

    public void resetActiveCode(UserResetActiveCodeCommand command) {
        User user = userRepository.findByUsername(command.username()).orElseThrow();
        if (user.isActive()) {
            throw new IllegalArgumentException("用户已激活，无需再次激活！");
        }
        user.generateActivationCode();
        userRepository.save(user);
    }

    public void active(UserActiveCommand command) {
        User user = userRepository.findByUsername(command.getUsername()).orElseThrow();

        authenticationSpecification.checkActiveCondition(user);
        if (!StrUtil.equals(user.getActivationCode(), command.getActiveCode())) {
            throw new IllegalArgumentException("激活码错误");
        }
        user.activateBySelf();
        userRepository.save(user);
    }

    public void activeFromAdmin(AdminActiveUserCommand command) {
        User user = userRepository.findUserByUserId(command.getUid()).orElseThrow();

        authenticationSpecification.checkActiveConditionForAdminActivation(user, command.getAdminUsername());
        user.activateByAdmin();
        userRepository.save(user);
    }

    public void deActiveFromAdmin(AdminDeActiveUserCommand command) {
        User user = userRepository.findUserByUserId(command.getUid()).orElseThrow();

        authenticationSpecification.checkDeActiveConditionForAdminActivation(user, command.getAdminUsername());
        user.deactivateByAdmin();
        userRepository.save(user);
    }

    /**
     * 更改邮箱
     */
    public void changeEmail(User user, Email newEmail) {
        validateEmailUniqueness(newEmail);
        user.setEmail(newEmail);
        // 需要重新验证邮箱
        user.setActive(false);
        user.generateActivationCode();

        // 生成新的激活码
        eventPublisher.publish(new EmailChangedEvent(user.getId(), newEmail));
    }

    private void validateEmailUniqueness(Email newEmail) {
        boolean exists = userRepository.existsByEmail(newEmail);
        if (exists) {
            throw new IllegalArgumentException("邮箱已存在");
        }
    }

    public Optional<User> loadUserById(Long userId) {
        return userRepository.findById(UserId.of(userId));
    }

    /**
     * 绑定用户
     *
     * @param command 注册用户命令
     */
    public void registerConnection(RegisterConnectionCommand command) {
        Optional<User> qingUser = userRepository.findByUsername(command.username());

        Assert.isTrue(qingUser.isPresent(), "用户不存在，不能绑定！");

        QingAuthUser qingAuthUser = command.qingAuthUser();

        if (qingUser.isPresent()) {
            User user = qingUser.get();

            UserConnection userConnection = UserConnection.builder()
                    .accessToken(qingAuthUser.getToken().getAccessToken())
                    .authProvider(qingAuthUser.getSource())
                    .displayName(qingAuthUser.getUsername())
                    .avatarUrl(qingAuthUser.getAvatar())
                    .refreshToken(qingAuthUser.getToken().getRefreshToken())
                    .providerUserId(qingAuthUser.getUuid())
                    .build();

            user.addConnection(userConnection);
            userRepository.save(user);
        }
    }

    public List<UserConnection> findConnectionByProviderIdAndProviderUserId(String providerId, String providerUserId) {
        return null;
    }

    /**
     * 批量验证用户名是否可用
     */
    public List<UsernameAvailability> checkUsernameAvailability(List<String> usernames) {
        return usernames.stream()
                .map(username -> {
                    try {
                        Username usernameObj = Username.of(username);
                        boolean available = !userRepository.existsByUsername(usernameObj);
                        return new UsernameAvailability(username, available);
                    } catch (IllegalArgumentException e) {
                        return new UsernameAvailability(username, false, e.getMessage());
                    }
                })
                .toList();
    }

    public void unLockAccount(Long userId) {
        Optional<User> userByUserId = userRepository.findUserByUserId(userId);
        if (userByUserId.isEmpty()) {
            throw new AuthenticationException("用户不存在");
        }
        User user = userByUserId.get();
        user.unlock();
        userRepository.save(user);
        eventPublisher.publishAll(user.domainEvents());
        user.clearDomainEvents();
    }

    public void lockAccount(Long userId) {
        User user = userRepository.findUserByUserId(userId).orElseThrow(() -> new AuthenticationException("用户不存在"));
        user.lockAccount(UserLockReason.EXCEEDED_MAX_ATTEMPTS, Duration.ofDays(1));
        userRepository.save(user);
        eventPublisher.publishAll(user.domainEvents());
    }

    public void refreshToken(@NotBlank(message = "刷新令牌不能为空") String refreshToken) {

    }
}
