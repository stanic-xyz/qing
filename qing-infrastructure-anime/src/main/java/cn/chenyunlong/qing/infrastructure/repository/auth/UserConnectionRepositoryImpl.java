package cn.chenyunlong.qing.infrastructure.repository.auth;

import cn.chenyunlong.qing.domain.auth.connection.UserConnection;
import cn.chenyunlong.qing.domain.auth.connection.repository.UserConnectionRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.auth.UserConnectionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserConnectionRepositoryImpl extends JpaServiceImpl<UserConnectionJpaRepository, UserConnection, Long> implements UserConnectionRepository {

    private final UserConnectionJpaRepository userConnectionJpaRepository;

    @Override
    public List<UserConnection> findConnectionByProviderIdAndProviderUserId(String providerId, String providerUserId) {
        return userConnectionJpaRepository.findConnectionByProviderIdAndProviderUserId(providerId, providerUserId);
    }
}
