package cn.chenyunlong.qing.infrastructure.repository.auth;

import cn.chenyunlong.qing.domain.auth.user.QingUser;
import cn.chenyunlong.qing.domain.auth.user.repository.QingUserRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.auth.QingUserJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class QingUserRepositoryImpl extends JpaServiceImpl<QingUserJpaRepository, QingUser, Long> implements QingUserRepository {

}
