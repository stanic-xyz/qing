package cn.chenyunlong.qing.infrastructure.repository.jpa;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.auth.admin.AdminAccountRoleRel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminAccountRoleJpaRepository extends BaseJpaRepository<AdminAccountRoleRel, Long> {

    @Query("select a from AdminAccountRoleRel a where a.adminAccountId = ?1")
    List<AdminAccountRoleRel> listRoleByAccountId(Long accountId);
}
