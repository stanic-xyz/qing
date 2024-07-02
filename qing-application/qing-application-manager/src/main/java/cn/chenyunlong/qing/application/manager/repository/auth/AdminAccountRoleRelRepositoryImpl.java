package cn.chenyunlong.qing.application.manager.repository.auth;

import cn.chenyunlong.qing.application.manager.repository.JpaServiceImpl;
import cn.chenyunlong.qing.application.manager.repository.jpa.AdminAccountRoleJpaRepository;
import cn.chenyunlong.qing.domain.auth.admin.AdminAccountRoleRel;
import cn.chenyunlong.qing.domain.auth.admin.repository.AdminAccountRoleRelRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAccountRoleRelRepositoryImpl extends JpaServiceImpl<AdminAccountRoleJpaRepository, AdminAccountRoleRel, Long> implements AdminAccountRoleRelRepository {

    private final AdminAccountRoleJpaRepository tagJpaRepository;

    /**
     * 根据账户查询角色列表
     *
     * @param accountId 账户id
     * @return 角色列凑
     */
    @Override
    public List<AdminAccountRoleRel> listRoleByAccountId(Long accountId) {
        return tagJpaRepository.listRoleByAccountId(accountId);
    }
}
