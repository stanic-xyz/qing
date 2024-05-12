package cn.chenyunlong.qing.domain.auth.admin.repository;

import cn.chenyunlong.jpa.support.BaseRepository;
import cn.chenyunlong.qing.domain.auth.admin.AdminAccountRoleRel;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface AdminAccountRoleRelRepository extends BaseRepository<AdminAccountRoleRel, Long> {

    /**
     * 根据账户查询角色列表
     *
     * @param accountId 账户id
     * @return 角色列凑
     */
    @Query("select a from AdminAccountRoleRel a where a.adminAccountId = ?1")
    List<AdminAccountRoleRel> listRoleByAccountId(Long accountId);

    void deleteAllByIds(List<Long> oldRoleIds);

    void saveAll(List<AdminAccountRoleRel> domainList);
}
