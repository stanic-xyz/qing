package cn.chenyunlong.qing.infrastructure.repository.jpa.auth;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.auth.connection.UserConnection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserConnectionJpaRepository extends BaseJpaRepository<UserConnection, Long> {

    @Query("select c from UserConnection c where c.providerId = ?1 and c.providerUserId = ?2")
    List<UserConnection> findConnectionByProviderIdAndProviderUserId(String providerId, String providerUserId);

}
