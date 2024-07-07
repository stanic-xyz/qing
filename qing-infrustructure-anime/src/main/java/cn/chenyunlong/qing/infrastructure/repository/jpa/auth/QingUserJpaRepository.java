package cn.chenyunlong.qing.infrastructure.repository.jpa.auth;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.auth.user.QingUser;
import org.springframework.stereotype.Repository;

@Repository
public interface QingUserJpaRepository extends BaseJpaRepository<QingUser, Long> {

}
