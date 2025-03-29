package cn.chenyunlong.qing.domain.auth.connection.repository;

import cn.chenyunlong.qing.domain.common.AggregateId;
import cn.chenyunlong.qing.domain.common.repository.BaseRepository;
import cn.chenyunlong.qing.domain.auth.connection.UserConnection;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserConnectionRepository extends BaseRepository<UserConnection, AggregateId> {

    List<UserConnection> findConnectionByProviderIdAndProviderUserId(String providerId,
                                                                     String providerUserId);
}
