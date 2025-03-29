package cn.chenyunlong.qing.infrastructure.anime.repository.jpa.repository.auth;

import cn.chenyunlong.jpa.support.BaseJpaQueryRepository;
import cn.chenyunlong.qing.domain.auth.user.UserToken;
import cn.chenyunlong.qing.infrastructure.anime.repository.jpa.entity.UserTokenEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenJpaRepository extends BaseJpaQueryRepository<UserTokenEntity, Long> {

}
