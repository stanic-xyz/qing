package cn.chenyunlong.qing.application.manager.repository.jpa;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.auth.role.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleJpaRepository extends BaseJpaRepository<Role, Long> {

}
