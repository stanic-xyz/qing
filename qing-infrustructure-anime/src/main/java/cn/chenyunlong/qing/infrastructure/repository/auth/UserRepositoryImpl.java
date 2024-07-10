package cn.chenyunlong.qing.infrastructure.repository.auth;

import cn.chenyunlong.qing.domain.auth.user.QingUser;
import cn.chenyunlong.qing.domain.auth.user.repository.UserRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.auth.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRepositoryImpl extends JpaServiceImpl<UserJpaRepository, QingUser, Long> implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public QingUser findByUsername(String username) {
        return userJpaRepository.findByUsername(username);
    }

    @Override
    public QingUser findUserByUserId(String userId) {
        return userJpaRepository.findUserByUserId(userId);
    }
}
