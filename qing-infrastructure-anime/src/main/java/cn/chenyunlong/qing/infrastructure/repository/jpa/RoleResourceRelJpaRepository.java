package cn.chenyunlong.qing.infrastructure.repository.jpa;

import cn.chenyunlong.jpa.support.BaseJpaRepository;
import cn.chenyunlong.qing.domain.auth.role.RoleResourceRel;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleResourceRelJpaRepository extends BaseJpaRepository<RoleResourceRel, Long> {

}
