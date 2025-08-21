package cn.chenyunlong.qing.infrastructure.auth.repository;

import cn.chenyunlong.qing.auth.domain.admin.AdminAccountRoleRel;
import cn.chenyunlong.qing.auth.domain.admin.repository.AdminAccountRoleRelRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminAccountRoleRelRepositoryImpl implements AdminAccountRoleRelRepository {
    @Override
    public List<AdminAccountRoleRel> listRoleByAccountId(Long accountId) {
        return List.of();
    }

    @Override
    public void deleteAllByIds(List<AggregateId> oldRoleIds) {

    }

    @Override
    public void saveAll(List<AdminAccountRoleRel> roleRelList) {

    }
}
