package cn.chenyunlong.qing.infrastructure.repository.auth;

import cn.chenyunlong.qing.domain.auth.user.QingUser;
import cn.chenyunlong.qing.domain.auth.user.repository.UserRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.auth.UserJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRepositoryImpl extends JpaServiceImpl<UserJpaRepository, QingUser, Long> implements UserRepository {

    @Override
    public QingUser findByUsername(String username) {
        return null;
    }

    @Override
    public QingUser findUserByUserId(String userId) {
        return null;
    }
}
