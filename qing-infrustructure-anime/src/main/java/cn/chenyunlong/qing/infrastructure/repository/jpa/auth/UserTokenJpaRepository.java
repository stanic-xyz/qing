package cn.chenyunlong.qing.infrastructure.repository.jpa.auth;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.auth.user.UserToken;

public interface UserTokenJpaRepository extends BaseJpaRepository<UserToken, Long> {

}
