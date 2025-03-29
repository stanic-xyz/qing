package cn.chenyunlong.qing.infrastructure.anime.repository;

import cn.chenyunlong.qing.domain.auth.connection.UserConnection;
import cn.chenyunlong.qing.domain.auth.connection.repository.UserConnectionRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.infrastructure.anime.repository.jpa.repository.auth.UserConnectionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserConnectionRepositoryImpl implements UserConnectionRepository {

    private final UserConnectionJpaRepository userConnectionJpaRepository;

    @Override
    public List<UserConnection> findConnectionByProviderIdAndProviderUserId(String providerId, String providerUserId) {
        return userConnectionJpaRepository.findConnectionByProviderIdAndProviderUserId(providerId, providerUserId);
    }

    @Override
    public UserConnection save(UserConnection entity) {
        return null;
    }

    @Override
    public Optional<UserConnection> findById(AggregateId id) {
        return Optional.empty();
    }
}
