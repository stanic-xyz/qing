package cn.chenyunlong.qing.auth.infrastructure.repository;

import cn.chenyunlong.qing.auth.domain.user.UserToken;
import cn.chenyunlong.qing.auth.domain.user.repository.UserTokenRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserTokenRepositoryImpl implements UserTokenRepository {

    @Override
    public UserToken save(UserToken entity) {
        return null;
    }

    @Override
    public Optional<UserToken> findById(AggregateId id) {
        return Optional.empty();
    }
}
