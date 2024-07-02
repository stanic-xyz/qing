package cn.chenyunlong.qing.application.manager.repository.auth;

import cn.chenyunlong.qing.application.manager.repository.JpaServiceImpl;
import cn.chenyunlong.qing.application.manager.repository.jpa.AdminAccountJpaRepository;
import cn.chenyunlong.qing.domain.auth.admin.AdminAccount;
import cn.chenyunlong.qing.domain.auth.admin.repository.AdminAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminAccountRepositoryImpl extends JpaServiceImpl<AdminAccountJpaRepository, AdminAccount, Long> implements AdminAccountRepository {

}
