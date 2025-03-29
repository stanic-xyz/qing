package cn.chenyunlong.qing.infrastructure.anime.repository;

import cn.chenyunlong.qing.domain.auth.admin.AdminAccountRoleRel;
import cn.chenyunlong.qing.domain.auth.admin.repository.AdminAccountRoleRelRepository;
import cn.chenyunlong.qing.domain.common.AggregateId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminAccountRoleRelRepositoryImpl implements AdminAccountRoleRelRepository {


    /**
     * 根据账户查询角色列表
     *
     * @param accountId 账户id
     * @return 角色列凑
     */
    @Override
    public List<AdminAccountRoleRel> listRoleByAccountId(Long accountId) {
        return null;
    }

    @Override
    public void deleteAllByIds(List<AggregateId> oldRoleIds) {

    }

    @Override
    public void saveAll(List<AdminAccountRoleRel> roleRelList) {

    }
}
