package cn.chenyunlong.qing.infrastructure.repository.auth;

import cn.chenyunlong.qing.domain.auth.user.UserToken;
import cn.chenyunlong.qing.domain.auth.user.repository.UserTokenRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.auth.UserTokenJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class UserTokenRepositoryImpl extends JpaServiceImpl<UserTokenJpaRepository, UserToken, Long> implements UserTokenRepository {

}
