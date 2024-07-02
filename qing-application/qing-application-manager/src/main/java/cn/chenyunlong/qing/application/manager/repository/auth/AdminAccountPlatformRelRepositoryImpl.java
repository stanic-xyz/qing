package cn.chenyunlong.qing.application.manager.repository.auth;

import cn.chenyunlong.qing.application.manager.repository.JpaServiceImpl;
import cn.chenyunlong.qing.application.manager.repository.jpa.AdminAccountPlatformRelJpaRepository;
import cn.chenyunlong.qing.domain.auth.admin.AdminAccountPlatformRel;
import cn.chenyunlong.qing.domain.auth.admin.repository.AdminAccountPlatformRelRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AdminAccountPlatformRelRepositoryImpl extends JpaServiceImpl<AdminAccountPlatformRelJpaRepository, AdminAccountPlatformRel, Long>
    implements AdminAccountPlatformRelRepository {

    @Override
    public List<AdminAccountPlatformRel> listPlatformsByAccountId(Long accountId) {
        return List.of();
    }
}
