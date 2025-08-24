package cn.chenyunlong.qing.auth.infrastructure.repository;

import cn.chenyunlong.qing.auth.domain.user.QingUser;
import cn.chenyunlong.qing.auth.domain.user.QingUserId;
import cn.chenyunlong.qing.auth.domain.user.repository.QingUserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QingUserRepositoryImpl implements QingUserRepository {

    @Override
    public QingUser save(QingUser entity) {
        return null;
    }

    @Override
    public Optional<QingUser> findById(QingUserId id) {
        return Optional.empty();
    }
}
