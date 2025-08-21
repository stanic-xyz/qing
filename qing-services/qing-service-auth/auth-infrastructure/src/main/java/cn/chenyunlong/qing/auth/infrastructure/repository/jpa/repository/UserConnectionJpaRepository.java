package cn.chenyunlong.qing.infrastructure.auth.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaQueryRepository;
import cn.chenyunlong.qing.auth.domain.user.UserConnection;
import cn.chenyunlong.qing.infrastructure.auth.repository.jpa.entity.UserConnectionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserConnectionJpaRepository extends BaseJpaQueryRepository<UserConnectionEntity, Long> {

    @Query("select c from UserConnectionEntity c where c.providerId = ?1 and c.providerUserId = ?2")
    List<UserConnection> findConnectionByProviderIdAndProviderUserId(String providerId, String providerUserId);

}
