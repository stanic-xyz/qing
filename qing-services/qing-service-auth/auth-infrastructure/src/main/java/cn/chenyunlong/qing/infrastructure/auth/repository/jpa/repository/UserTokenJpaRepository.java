package cn.chenyunlong.qing.infrastructure.auth.repository.jpa.repository;

import cn.chenyunlong.jpa.support.BaseJpaQueryRepository;
import cn.chenyunlong.qing.infrastructure.auth.repository.jpa.entity.UserTokenEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenJpaRepository extends BaseJpaQueryRepository<UserTokenEntity, Long> {

}
