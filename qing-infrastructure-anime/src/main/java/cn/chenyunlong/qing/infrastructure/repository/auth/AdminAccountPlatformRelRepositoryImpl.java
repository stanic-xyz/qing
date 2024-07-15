package cn.chenyunlong.qing.infrastructure.repository.auth;

import cn.chenyunlong.qing.domain.auth.admin.AdminAccountPlatformRel;
import cn.chenyunlong.qing.domain.auth.admin.repository.AdminAccountPlatformRelRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.AdminAccountPlatformRelJpaRepository;
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
