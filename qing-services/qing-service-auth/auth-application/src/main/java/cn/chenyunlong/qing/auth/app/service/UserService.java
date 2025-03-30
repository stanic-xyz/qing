package cn.chenyunlong.qing.auth.app.service;

import cn.chenyunlong.qing.auth.domain.user.QingUser;
import cn.chenyunlong.qing.auth.domain.user.UserConnection;
import cn.chenyunlong.qing.auth.domain.user.dto.creator.UserCreator;
import cn.chenyunlong.qing.auth.domain.user.dto.entity.QingAuthUser;
import cn.chenyunlong.qing.auth.domain.user.repository.UserRepository;
import cn.chenyunlong.qing.domain.base.EntityOperations;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.BaseAggregate;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * createImpl
     */
    public Optional<QingUser> register(UserCreator creator) {
        QingUser byUsername = userRepository.findByUsername(creator.getUsername());
        Assert.isNull(byUsername, "用户名已存在");
        QingUser byEmail = userRepository.findByEmail(creator.getEmail());
        Assert.isNull(byEmail, "邮箱已存在");
        return EntityOperations.doCreate(userRepository)
            .create(() -> QingUser.register(new AggregateId(IdUtil.getSnowflakeNextId()),
                creator.getUsername(),
                creator.getPassword(),
                userRepository))
            .update(QingUser::init)
            .errorHook(Throwable::printStackTrace)
            .execute();
    }


    public void validUser(Long id) {
        EntityOperations.doUpdate(userRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::valid)
            .execute();
    }


    public void invalidUser(Long id) {
        EntityOperations.doUpdate(userRepository)
            .loadById(new AggregateId(id))
            .update(BaseAggregate::invalid)
            .execute();
    }


    public Optional<QingUser> findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    /**
     * 根据用户名查询用户
     */

    public Optional<QingUser> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }


    public Optional<QingUser> loadUserById(Long userId) {
        return userRepository.findById(new AggregateId(userId));
    }


    public List<QingUser> findByUserNickNames(Set<String> nickNames) {
        return userRepository.findByUserNames(nickNames);
    }


    /**
     * 绑定用户
     *
     * @param username     用户名
     * @param qingAuthUser 第三方用户
     */
    public void registerConnection(String username, QingAuthUser qingAuthUser) {
        QingUser qingUse = userRepository.findByUsername(username);

        Assert.notNull(qingUse, "用户不存在，不能绑定！");

        UserConnection userConnection = UserConnection.builder()
            .accessToken(qingAuthUser.getToken().getAccessToken())
            .authProvider(qingAuthUser.getSource())
            .displayName(qingAuthUser.getUsername())
            .avatarUrl(qingAuthUser.getAvatar())
            .refreshToken(qingAuthUser.getToken().getRefreshToken())
            .providerUserId(qingAuthUser.getUuid())
            .build();

        qingUse.addConnection(userConnection);
        userRepository.save(qingUse);
    }


    public List<UserConnection> findConnectionByProviderIdAndProviderUserId(String providerId, String providerUserId) {
        return null;
    }
}
