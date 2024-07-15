package cn.chenyunlong.qing.infrastructure.repository.jpa;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.auth.admin.AdminAccount;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminAccountJpaRepository extends BaseJpaRepository<AdminAccount, Long> {

}
