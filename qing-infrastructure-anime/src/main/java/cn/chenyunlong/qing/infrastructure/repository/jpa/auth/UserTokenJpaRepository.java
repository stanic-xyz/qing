package cn.chenyunlong.qing.infrastructure.repository.jpa.auth;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.auth.user.UserToken;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenJpaRepository extends BaseJpaRepository<UserToken, Long> {

}
