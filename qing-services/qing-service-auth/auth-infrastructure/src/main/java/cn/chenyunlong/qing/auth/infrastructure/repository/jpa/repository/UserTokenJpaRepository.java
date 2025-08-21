package cn.chenyunlong.qing.auth.infrastructure.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaQueryRepository;
import cn.chenyunlong.qing.auth.infrastructure.repository.jpa.entity.UserTokenEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenJpaRepository extends BaseJpaQueryRepository<UserTokenEntity, Long> {

}
