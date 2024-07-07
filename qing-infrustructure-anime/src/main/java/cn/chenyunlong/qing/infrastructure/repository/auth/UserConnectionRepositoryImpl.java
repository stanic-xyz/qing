package cn.chenyunlong.qing.infrastructure.repository.auth;

import cn.chenyunlong.qing.domain.auth.connection.UserConnection;
import cn.chenyunlong.qing.domain.auth.connection.repository.UserConnectionRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.auth.UserConnectionJpaRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserConnectionRepositoryImpl extends JpaServiceImpl<UserConnectionJpaRepository, UserConnection, Long> implements UserConnectionRepository {


    @Override
    public List<UserConnection> findConnectionByProviderIdAndProviderUserId(String providerId, String providerUserId) {
        return List.of();
    }
}
