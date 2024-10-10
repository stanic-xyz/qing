package cn.chenyunlong.qing.infrastructure.repository.auth;

import cn.chenyunlong.qing.domain.auth.admin.AdminAccountRoleRel;
import cn.chenyunlong.qing.domain.auth.admin.repository.AdminAccountRoleRelRepository;
import cn.chenyunlong.qing.infrastructure.repository.base.JpaServiceImpl;
import cn.chenyunlong.qing.infrastructure.repository.jpa.AdminAccountRoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
