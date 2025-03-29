package cn.chenyunlong.qing.infrastructure.anime.repository;

import cn.chenyunlong.qing.domain.auth.user.UserToken;
import cn.chenyunlong.qing.domain.auth.user.repository.UserTokenRepository;
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
