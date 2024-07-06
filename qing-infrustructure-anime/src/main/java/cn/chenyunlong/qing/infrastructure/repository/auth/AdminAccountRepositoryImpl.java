package cn.chenyunlong.qing.infrastructure.repository.auth;

import cn.chenyunlong.qing.domain.auth.admin.AdminAccount;
import cn.chenyunlong.qing.domain.auth.admin.repository.AdminAccountRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.AdminAccountJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminAccountRepositoryImpl extends JpaServiceImpl<AdminAccountJpaRepository, AdminAccount, Long> implements AdminAccountRepository {


}
