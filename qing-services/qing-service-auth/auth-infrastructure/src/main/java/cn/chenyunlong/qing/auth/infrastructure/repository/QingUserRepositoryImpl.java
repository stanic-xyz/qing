package cn.chenyunlong.qing.auth.infrastructure.repository;

import cn.chenyunlong.qing.auth.domain.user.QingUser;
import cn.chenyunlong.qing.auth.domain.user.repository.QingUserRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QingUserRepositoryImpl implements QingUserRepository {

    @Override
    public QingUser save(QingUser entity) {
        return null;
    }

    @Override
    public Optional<QingUser> findById(AggregateId id) {
        return Optional.empty();
    }
}
