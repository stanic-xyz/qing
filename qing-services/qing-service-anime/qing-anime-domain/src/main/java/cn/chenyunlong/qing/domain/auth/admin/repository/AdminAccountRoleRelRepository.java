package cn.chenyunlong.qing.domain.auth.admin.repository;

import cn.chenyunlong.qing.domain.auth.admin.AdminAccountRoleRel;
import cn.chenyunlong.qing.domain.common.AggregateId;

import java.util.List;

public interface AdminAccountRoleRelRepository {

    /**
     * 根据账户查询角色列表
     *
     * @param accountId 账户id
     * @return 角色列凑
     */
    List<AdminAccountRoleRel> listRoleByAccountId(Long accountId);

    void deleteAllByIds(List<AggregateId> oldRoleIds);

    void saveAll(List<AdminAccountRoleRel> roleRelList);
}
