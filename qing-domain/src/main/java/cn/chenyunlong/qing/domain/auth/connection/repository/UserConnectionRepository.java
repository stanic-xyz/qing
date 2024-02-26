package cn.chenyunlong.qing.domain.auth.connection.repository;

import cn.chenyunlong.jpa.support.BaseRepository;
import cn.chenyunlong.qing.domain.auth.connection.UserConnection;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface UserConnectionRepository extends BaseRepository<UserConnection, Long> {

    @Query("select c from UserConnection c where c.providerId = ?1 and c.providerUserId = ?2")
    List<UserConnection> findConnectionByProviderIdAndProviderUserId(String providerId,
        String providerUserId);
}
